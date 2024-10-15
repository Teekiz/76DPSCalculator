package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Perk} objects.
 */
@Service
@Getter
@Slf4j
public class PerkManager
{
	private final PerkLoaderService perkLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link PerkManager} object.
	 * @param perkLoaderService A service used to load {@link Perk} objects.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
		this.perkLoaderService = perkLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}
	//when a perk is added - it is automatically added to the effects.
	@SaveLoadout
	public void addPerk(String perkName, Loadout loadout) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		log.debug("Adding {} to loadout {}.", perk.name(), loadout.getLoadoutID());
		loadout.getPerks().put(perk, modifierConditionLogic.evaluateCondition(perk, loadout));

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been added");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}

	//todo - consider changing from different perkNames (as the list doesn't match)
	@SaveLoadout
	public void removePerk(String perkName, Loadout loadout) throws IOException
	{
		Perk perk = loadout.getPerks()
			.keySet().stream()
			.filter(key -> key.name().equalsIgnoreCase(perkName))
			.findFirst()
			.orElse(null);
		if (perk != null)
		{
			loadout.getPerks().remove(perk);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been removed");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}

	/**
	 * An event listener used to receive events if a
	 * @param event An event that is called when a change has been made to a {@link Loadout}'s weapon.
	 */
	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		Loadout loadout = event.getLoadout();
		for (Map.Entry<Perk, Boolean> entry : loadout.getPerks().entrySet())
		{
			Boolean newValue = modifierConditionLogic.evaluateCondition(entry.getKey(), loadout);
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}
}

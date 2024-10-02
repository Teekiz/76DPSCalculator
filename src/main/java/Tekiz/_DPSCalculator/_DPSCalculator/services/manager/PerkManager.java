package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Perk} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
@Slf4j
public class PerkManager implements LoadoutScopeClearable
{
	private HashMap<Perk, Boolean> perks;
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
		this.perks = new HashMap();
		this.perkLoaderService = perkLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	//when a perk is added - it is automatically added to the effects.
	public void addPerk(String perkName) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		perks.put(perk, modifierConditionLogic.evaluateCondition(perk));

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, perk.getName() + " has been added");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}

	//todo - consider changing from different perkNames (as the list doesn't match)
	public void removePerk(String perkName) throws IOException
	{
		Perk perk = perks.keySet().stream()
			.filter(key -> key.getName().equals(perkName))
			.findFirst()
			.orElse(null);
		if (perk != null)
		{
			perks.remove(perk);

			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, perk.getName() + " has been removed");
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
		for (Map.Entry<Perk, Boolean> entry : perks.entrySet())
		{
			Boolean newValue = modifierConditionLogic.evaluateCondition(entry.getKey());
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}

	/**
	 * A method used during the cleanup of this service.
	 */
	@PreDestroy
	public void clear()
	{
		if (this.perks != null) {
			this.perks.clear();
		}
		this.perks = null;
	}
}

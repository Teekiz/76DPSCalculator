package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
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
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;

/**
 * A service used to manage {@link Consumable} objects.
 */
@Service
@Getter
@Slf4j
public class ConsumableManager
{
	private final ConsumableLoaderService consumableLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link ConsumableManager} object.
	 * @param consumableLoaderService A service used to load {@link Consumable}.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
		this.consumableLoaderService = consumableLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}
	public void addConsumable(String consumableName, Loadout loadout) throws IOException
	{
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		loadout.getConsumables().put(consumable, modifierConditionLogic.evaluateCondition(consumable, loadout));

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, loadout,consumable.getName() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	public void removeConsumable(String consumableName, Loadout loadout) throws IOException
	{
		Consumable consumable = loadout.getConsumables()
				.keySet().stream()
				.filter(key -> key.getName().equals(consumableName))
				.findFirst()
				.orElse(null);
		if (consumable != null)
		{
			loadout.getConsumables().remove(consumable);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, loadout,consumable.getName() + " has been removed.");
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
		for (Map.Entry<Consumable, Boolean> entry : loadout.getConsumables().entrySet())
		{
			Boolean newValue = modifierConditionLogic.evaluateCondition(entry.getKey(), loadout);
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}
}

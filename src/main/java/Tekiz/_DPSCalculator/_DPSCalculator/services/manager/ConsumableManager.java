package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
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
	private final DataLoaderService dataLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link ConsumableManager} object.
	 * @param dataLoaderService A service used to load {@link Consumable}.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public ConsumableManager(DataLoaderService dataLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
		this.modifierConditionLogic = modifierConditionLogic;
	}
	@SaveLoadout
	public void addConsumable(String consumableID, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Consumable consumable = dataLoaderService.loadData(consumableID, Consumable.class, null);

		if (consumable == null){
			log.error("Consumable loading failed for: {}", consumableID);
			throw new ResourceNotFoundException("Consumable not found. ID: " + consumableID  + ".");
		}

		log.debug("Adding {} to loadout {}.", consumable.name(), loadout.getLoadoutID());
		loadout.getConsumables().put(consumable, modifierConditionLogic.evaluateCondition(consumable, loadout));

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, loadout,consumable.name() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	@SaveLoadout
	public void removeConsumable(String consumableName, Loadout loadout) throws IOException
	{
		Consumable consumable = loadout.getConsumables()
				.keySet().stream()
				.filter(key -> key.name().equalsIgnoreCase(consumableName))
				.findFirst()
				.orElse(null);
		if (consumable != null)
		{
			loadout.getConsumables().remove(consumable);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, loadout,consumable.name() + " has been removed.");
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

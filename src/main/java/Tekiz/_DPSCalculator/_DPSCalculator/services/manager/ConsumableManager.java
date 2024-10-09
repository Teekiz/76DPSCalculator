package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
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
	//there can be many food effects but only one of each alcohol and chem.
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
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
	public HashMap<Consumable, Boolean> getConsumables()
	{
		return getLoadoutManager().getActiveLoadout().getConsumables();
	}
	public void addConsumable(String consumableName) throws IOException
	{
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		getConsumables().put(consumable, modifierConditionLogic.evaluateCondition(consumable));

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, consumable.getName() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	public void removeConsumable(String consumableName) throws IOException
	{
		Consumable consumable = getConsumables()
				.keySet().stream()
				.filter(key -> key.getName().equals(consumableName))
				.findFirst()
				.orElse(null);
		if (consumable != null)
		{
			getConsumables().remove(consumable);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(consumable, consumable.getName() + " has been removed.");
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
		for (Map.Entry<Consumable, Boolean> entry :getConsumables().entrySet())
		{
			Boolean newValue = modifierConditionLogic.evaluateCondition(entry.getKey());
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}
}

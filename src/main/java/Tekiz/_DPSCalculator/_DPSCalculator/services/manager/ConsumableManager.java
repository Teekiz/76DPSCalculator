package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;

/**
 * A service used to manage {@link Consumable} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class ConsumableManager implements LoadoutScopeClearable
{
	//there can be many food effects but only one of each alcohol and chem.
	private HashMap<Consumable, Boolean> consumables;
	private final ConsumableLoaderService consumableLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private static final Logger logger = LoggerFactory.getLogger(ConsumableManager.class);

	/**
	 * The constructor for a {@link ConsumableManager} object.
	 * @param consumableLoaderService A service used to load {@link Consumable}.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService, ModifierConditionLogic modifierConditionLogic)
	{
		this.consumables = new HashMap();
		this.consumableLoaderService = consumableLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	public void addConsumable(String consumableName) throws IOException
	{
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		consumables.put(consumable, modifierConditionLogic.evaluateCondition(consumable));
	}
	public void removeConsumable(String consumableName) throws IOException
	{
		consumables.entrySet().removeIf(consumable -> consumable.getKey().getName().equals(consumableName));
	}

	/**
	 * An event listener used to receive events if a
	 * @param event An event that is called when a change has been made to a {@link Loadout}'s weapon.
	 */
	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		for (Map.Entry<Consumable, Boolean> entry : consumables.entrySet())
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
		if (this.consumables != null) {
			this.consumables.clear();
		}
		this.consumables = null;
	}
}

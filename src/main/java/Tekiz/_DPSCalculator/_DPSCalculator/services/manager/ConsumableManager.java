package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierLogic;
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

@Service
@Scope(scopeName = "loadout")
@Getter
public class ConsumableManager implements LoadoutScopeClearable
{
	//there can be many food effects but only one of each alcohol and chem.
	private HashMap<Consumable, Boolean> consumables;
	private final ConsumableLoaderService consumableLoaderService;
	private final ModifierLogic modifierLogic;
	private static final Logger logger = LoggerFactory.getLogger(ConsumableManager.class);

	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService, ModifierLogic modifierLogic)
	{
		this.consumables = new HashMap();
		this.consumableLoaderService = consumableLoaderService;
		this.modifierLogic = modifierLogic;
	}

	public void addConsumable(String consumableName) throws IOException
	{
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		consumables.put(consumable, modifierLogic.evaluateCondition(consumable));
	}
	public void removeConsumable(String consumableName) throws IOException
	{
		consumables.entrySet().removeIf(consumable -> consumable.getKey().getName().equals(consumableName));
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		for (Map.Entry<Consumable, Boolean> entry : consumables.entrySet())
		{
			Boolean newValue = modifierLogic.evaluateCondition(entry.getKey());
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}

	@PreDestroy
	public void clear()
	{
		if (this.consumables != null) {
			this.consumables.clear();
		}
		this.consumables = null;
	}
}

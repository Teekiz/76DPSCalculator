package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable.ConsumableLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
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
	private Set<Consumable> consumables;
	private final ConsumableLoaderService consumableLoaderService;
	private final ConsumableLogic consumableLogic;

	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService, ConsumableLogic consumableLogic)
	{
		this.consumables = new HashSet<>();
		this.consumableLoaderService = consumableLoaderService;
		this.consumableLogic = consumableLogic;
	}

	public void addConsumable(Consumable consumable)
	{
		consumables.add(consumable);
	}
	public void removeConsumable(Consumable consumable)
	{
		consumables.remove(consumable);
	}

	public boolean checkConsumable(Consumable consumable)
	{
		return consumableLogic.evaluateCondition(consumable);
	}

	public void checkAndApplyConsumable(Consumable consumable)
	{
		if (consumableLogic.evaluateCondition(consumable))
		{
			consumableLogic.applyConditionEffect(consumable);
		}
		consumableLogic.applyEffect(consumable);
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{

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

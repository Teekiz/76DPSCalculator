package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponModifiedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponModifiedListener;
import jakarta.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class ConsumableManager implements WeaponModifiedListener
{
	//there can be many food effects but only one of each alcohol and chem.
	private Set<Consumable> consumables;
	private final ConsumableLoaderService consumableLoaderService;

	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService)
	{
		this.consumables = new HashSet<>();
		this.consumableLoaderService = consumableLoaderService;
	}

	/*
	public void addConsumable(Consumable consumable)
	{
		consumables.add(consumable);
	}
	public void removeConsumable(Consumable consumable)
	{
		consumables.remove(consumable);
	}

	 */

	@Override
	public void onWeaponModified(WeaponModifiedEvent event)
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

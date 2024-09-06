package Tekiz._DPSCalculator._DPSCalculator.model.character.Player;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Character.ConsumableType;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Player
{
	private Special specials;
	private int level;
	//todo
	private int maxHP;
	private Set<Perk> perks;

	//there can be many food effects but only one of each alcohol and chem.
	private Set<Consumable> consumables;
	private Consumable alcoholConsumed;
	private Consumable chemConsumed;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	public Player()
	{
		this.level = 1;
		specials = new Special(1, 1, 1, 1, 1, 1, 1, 1, 15);
		perks = new HashSet<>();
		consumables = new HashSet<>();
	}

	//todo - consider not increasing the specials directly through mods, but add bonuses instead.

	//used to update the specials values based on enum provided. checks to make sure that specials cannot go over 100 or below 1.

	//todo - implement the specifics for armour slots, consumables and specials

	public void addConsumable(Consumable consumable)
	{
		if (consumable.getConsumableType() == ConsumableType.FOOD || consumable.getConsumableType() == ConsumableType.DRINK)
		{
			consumables.add(consumable);
		}
		else
		{
			if (consumable.getConsumableType() == ConsumableType.ALCOHOL)
			{
				alcoholConsumed = consumable;
			}
			else if (consumable.getConsumableType() == ConsumableType.CHEMS)
			{
				chemConsumed = consumable;
			}
		}
	}

	public void removeConsumable(Consumable consumable)
	{
		if (consumable.getConsumableType() == ConsumableType.FOOD || consumable.getConsumableType() == ConsumableType.DRINK)
		{
			consumables.remove(consumable);
		}
		else
		{
			if (consumable.getConsumableType() == ConsumableType.ALCOHOL)
			{
				alcoholConsumed = null;
			}
			else if (consumable.getConsumableType() == ConsumableType.CHEMS)
			{
				chemConsumed = null;
			}
		}
	}
}

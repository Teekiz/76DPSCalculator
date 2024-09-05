package Tekiz._DPSCalculator._DPSCalculator.model.character.Player;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.ConsumableType;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Player
{
	private int strength;
	private int perception;
	private int endurance;
	private int charisma;
	private int intelligence;
	private int agility;
	private int luck;

	private int level;
	private Set<Perk> perks;

	//there can be many food effects but only one of each alcohol and chem.
	private Set<Consumable> consumables;
	private Consumable alcoholConsumed;
	private Consumable chemConsumed;

	private int maxSpecialValue = 100;
	private int minSpecialValue = 1;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	public Player()
	{
		this.strength = this.perception = this.endurance = this.charisma = this.intelligence = this.agility = this.luck = 1;
		this.level = 1;
		perks = new HashSet<>();
		consumables = new HashSet<>();
	}

	//todo - consider not increasing the specials directly through mods, but add bonuses instead.

	//used to update the specials values based on enum provided. checks to make sure that specials cannot go over 100 or below 1.
	public void modifySpecial(Special special, int value)
	{
		switch (special)
		{
			case STRENGTH -> strength = modifySpecialCheck(strength, value);
			case PERCEPTION -> perception = modifySpecialCheck(perception, value);
			case ENDURANCE -> endurance = modifySpecialCheck(endurance, value);
			case CHARISMA -> charisma = modifySpecialCheck(charisma, value);
			case INTELLIGENCE -> intelligence = modifySpecialCheck(intelligence, value);
			case AGILITY -> agility = modifySpecialCheck(agility, value);
			case LUCK -> luck = modifySpecialCheck(luck, value);
		}
	}

	public int modifySpecialCheck(int current, int value)
	{
		if (current + value > maxSpecialValue) return maxSpecialValue;
		else return Math.max(current + value, minSpecialValue);
	}

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

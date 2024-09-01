package Tekiz._DPSCalculator._DPSCalculator.model.character.Player;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
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
	private List<Perk> perks;
	private List<Consumable> consumables;

	private int maxSpecialValue = 100;
	private int minSpecialValue = 1;

	public Player()
	{
		this.strength = this.perception = this.endurance = this.charisma = this.intelligence = this.agility = this.luck = 1;
		this.level = 1;
		perks = new ArrayList<>();
		consumables = new ArrayList<>();
	}

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
}

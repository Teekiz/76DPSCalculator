package Tekiz._DPSCalculator._DPSCalculator.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Special
{
	private int strength;
	private int perception;
	private int endurance;
	private int charisma;
	private int intelligence;
	private int agility;
	private int luck;

	private int minSpecialValue;
	private int maxSpecialValue;

	public void modifySpecial(Specials special, int value)
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

	private int modifySpecialCheck(int current, int value)
	{
		if (current + value > maxSpecialValue) return maxSpecialValue;
		else return Math.max(current + value, minSpecialValue);
	}
}

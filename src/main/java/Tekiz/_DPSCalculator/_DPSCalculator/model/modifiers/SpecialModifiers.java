package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.BonusTypes;
import lombok.Getter;

@Getter
public class SpecialModifiers
{
	private int strength;
	private int perception;
	private int endurance;
	private int charisma;
	private int intelligence;
	private int agility;
	private int luck;

	public SpecialModifiers()
	{
		this.strength = this.perception = this.endurance = this.charisma = this.intelligence = this.agility = this.luck = 0;
	}

	public void addSpecialBonus(BonusTypes bonusTypes, double addedBonus)
	{
		int bonus = (int) Math.round(addedBonus);

		switch (bonusTypes)
		{
			case SPECIAL_STRENGTH -> strength+=bonus;
			case SPECIAL_PERCEPTION -> perception+=bonus;
			case SPECIAL_ENDURANCE -> endurance+=bonus;
			case SPECIAL_CHARISMA -> charisma+=bonus;
			case SPECIAL_INTELLIGENCE -> intelligence+=bonus;
			case SPECIAL_AGILITY -> agility+=bonus;
			case SPECIAL_LUCK -> luck+=bonus;
			default -> {}
		}
	}

	public void removeSpecialBonus(BonusTypes bonusTypes, double addedBonus)
	{
		int bonus = (int) Math.round(addedBonus);

		switch (bonusTypes)
		{
			case SPECIAL_STRENGTH -> strength-=bonus;
			case SPECIAL_PERCEPTION -> perception-=bonus;
			case SPECIAL_ENDURANCE -> endurance-=bonus;
			case SPECIAL_CHARISMA -> charisma-=bonus;
			case SPECIAL_INTELLIGENCE -> intelligence-=bonus;
			case SPECIAL_AGILITY -> agility-=bonus;
			case SPECIAL_LUCK -> luck-=bonus;
			default -> {}
		}
	}
}

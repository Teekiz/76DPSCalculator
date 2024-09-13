package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.BonusTypes;
import java.util.HashMap;
import lombok.Getter;
@Getter
public class Modifiers
{
	//all data will be checked at the same time
	private final HashMap<BonusTypes, Double> modifiers;

	public Modifiers()
	{
		this.modifiers = new HashMap<>();
	}

	//if the bonus type has already been added, don't add this bonus
	public boolean addModifier(BonusTypes bonusType, Double bonus)
	{
		if (modifiers.containsKey(bonusType)){
			return false;
		}
		else {
			modifiers.put(bonusType, bonus);
			return true;
		}
	}
}


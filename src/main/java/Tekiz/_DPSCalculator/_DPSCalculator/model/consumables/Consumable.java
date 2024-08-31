package Tekiz._DPSCalculator._DPSCalculator.model.consumables;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.AddictionType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.ConsumableType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Consumable
{
	//todo - possibly add in condition check
	/*
		Addiction type is used to determine if the effect should stack or not.
	 */
	private String consumableName;
	private ConsumableType consumableType;
	private AddictionType addictionType;
	private String consumableEffect;
}

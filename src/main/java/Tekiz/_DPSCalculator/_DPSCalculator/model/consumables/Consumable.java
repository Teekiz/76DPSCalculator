package Tekiz._DPSCalculator._DPSCalculator.model.consumables;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.AddictionType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.ConsumableType;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable.ConsumableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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
	private String condition;
	private String conditionEffect;
	private String consumableEffect;

	@Autowired
	private ConsumableLogic consumableLogic;

	public boolean checkConsumable()
	{
		return consumableLogic.evaluateCondition(this);
	}
	public void checkAndApplyConsumable()
	{
		if (consumableLogic.evaluateCondition(this))
		{
			consumableLogic.applyConditionEffect(this);
		}
		consumableLogic.applyEffect(this);
	}
}

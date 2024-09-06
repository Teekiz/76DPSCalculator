package Tekiz._DPSCalculator._DPSCalculator.util.evaluationcontext;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.ReceiverType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class BaseEvaluationContext
{
	//This is used to set an alias for enums that may be found in the perk data.
	public static StandardEvaluationContext getBaseEvaluationContext(Object rootObject)
	{
		StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
		context.setVariable("WeaponType", WeaponType.class);
		context.setVariable("DamageType", DamageType.class);
		context.setVariable("ReceiverType", ReceiverType.class);
		context.setVariable("Special", Specials.class);

		return context;
	}
}

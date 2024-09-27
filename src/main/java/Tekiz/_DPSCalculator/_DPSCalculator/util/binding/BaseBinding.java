package Tekiz._DPSCalculator._DPSCalculator.util.binding;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import groovy.lang.Binding;

public class BaseBinding
{
	public static Binding getBaseBinding()
	{
		Binding binding = new Binding();
		binding.setVariable("WeaponType", WeaponType.class);
		binding.setVariable("DamageType", DamageType.class);
		binding.setVariable("ReceiverType", ReceiverType.class);
		binding.setVariable("Special", Specials.class);
		binding.setVariable("ModifierType", ModifierTypes.class);
		return binding;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.util.binding;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * A utility service to provide binding used by the {@link GroovyShell} instance.
 */
public class BaseBinding
{
	/**
	 * A method to return the provided binding context.
	 * @return A new {@link Binding} object with applied context.
	 */
	public static Binding getBaseBinding()
	{
		Binding binding = new Binding();
		binding.setVariable("WeaponType", WeaponType.class);
		binding.setVariable("DamageType", DamageType.class);
		binding.setVariable("ModSubType", ModSubType.class);
		binding.setVariable("Special", Specials.class);
		binding.setVariable("ModifierType", ModifierTypes.class);
		return binding;
	}
}

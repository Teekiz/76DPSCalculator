package Tekiz._DPSCalculator._DPSCalculator.util.contextual;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyTags;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.AttackType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.util.map.MapUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * A utility service to provide a {@link StandardEvaluationContext} object used by the {@link SpelExpressionParser} instance and binding for the {@link GroovyShell} instance.
 */
public class Context
{
	//This is used to set an alias for enums that may be found in the perk data.
	/**
	 * Creates and configures a {@link StandardEvaluationContext} with a given root object.
	 * This context is preloaded with various commonly used variables such as enums and utility classes.
	 *
	 * @param rootObject The root object to be used in the evaluation context. It can be {@code null} if no specific root is needed.
	 * @return A configured {@link StandardEvaluationContext} with predefined variables for SpEL evaluation.
	 */
	public static StandardEvaluationContext getBaseEvaluationContext(Object rootObject)
	{
		StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
		context.setVariable("WeaponType", WeaponType.class);
		context.setVariable("DamageType", DamageType.class);
		context.setVariable("ModType", ModType.class);
		context.setVariable("ModSubType", ModSubType.class);
		context.setVariable("Special", Specials.class);
		context.setVariable("ModifierType", ModifierType.class);
		context.setVariable("ModifierSource", ModifierSource.class);
		context.setVariable("MapUtil", MapUtil.class);
		context.setVariable("AttackType", AttackType.class);
		context.setVariable("EnemyType", EnemyType.class);
		context.setVariable("EnemyTags", EnemyTags.class);
		return context;
	}

	/**
	 * A method to return the provided binding context.
	 * @return A new {@link Binding} object with applied context.
	 */
	public static Binding getBaseBinding()
	{
		Binding binding = new Binding();
		binding.setVariable("WeaponType", WeaponType.class);
		binding.setVariable("DamageType", DamageType.class);
		binding.setVariable("ModType", ModType.class);
		binding.setVariable("ModSubType", ModSubType.class);
		binding.setVariable("Special", Specials.class);
		binding.setVariable("AttackType", AttackType.class);
		binding.setVariable("ModifierType", ModifierType.class);
		return binding;
	}
}

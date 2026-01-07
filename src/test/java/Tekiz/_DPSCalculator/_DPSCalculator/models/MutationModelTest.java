package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.MutationEffects;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class MutationModelTest extends BaseTestClass
{
	@Autowired
	SpelExpressionParser expressionParser;

	@Test
	public void testMutationEffects()
	{
		Expression expression = expressionParser.parseExpression("true");

		HashMap<ModifierType, ModifierValue<?>> positiveEffects = new HashMap<>();
		positiveEffects.put(ModifierType.DAMAGE_ADDITIVE, new ModifierValue<>(ModifierType.DAMAGE_ADDITIVE, 2.0));

		HashMap<ModifierType, ModifierValue<?>> negativeEffects = new HashMap<>();
		negativeEffects.put(ModifierType.HEALTH, new ModifierValue<>(ModifierType.HEALTH, -2.0));

		MutationEffects mutationEffectsPositive = new MutationEffects("", ModifierSource.MUTATION_POSITIVE, expression, positiveEffects);
		MutationEffects mutationEffectsNegative = new MutationEffects("", ModifierSource.MUTATION_NEGATIVE, expression, negativeEffects);
		Mutation mutation = new Mutation("1", "TEST", "", mutationEffectsPositive, mutationEffectsNegative);

		List<Modifier> values = mutation.aggregateMutationEffects();

		Double positiveEffect = (Double) values.stream()
			.filter(m -> m.modifierSource().equals(ModifierSource.MUTATION_POSITIVE))
			.findFirst().get().effects().values().stream().findFirst().get().getValue();

		Double negativeEffect = (Double) values.stream()
			.filter(m -> m.modifierSource().equals(ModifierSource.MUTATION_NEGATIVE))
			.findFirst().get().effects().values().stream().findFirst().get().getValue();

		assertEquals(2.0, positiveEffect);
		assertEquals(-2.0, negativeEffect);
	}
}

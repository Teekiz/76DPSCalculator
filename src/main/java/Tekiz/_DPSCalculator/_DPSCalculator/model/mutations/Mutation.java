package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Value;

/** Represents a mutation that a player character might have. */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Mutation implements Serializable
{
	String name;
	String description;
	/** Stores the positive effects a mutation has. **/
	MutationEffects positiveEffects;
	/** Stores the negative effects a mutation has. **/
	MutationEffects negativeEffects;

	/**
	 * A method that aggregates and returns all positive and negative {@link Mutation} {@link Modifier}s.
	 * @return A {@link HashMap} of {@link Modifier}s and condition values.
	 */
	//todo - something is causing this to break when the name is changed (getMutationModifiers)
	public HashMap<Modifier, Boolean> aggregateMutationEffects()
	{
		HashMap<Modifier, Boolean> mutationMap = new HashMap<>();
		if (negativeEffects != null) {
			mutationMap.put(negativeEffects, true);
		}
		if (positiveEffects != null) {
			mutationMap.put(positiveEffects, true);
		}
		return mutationMap;
	}
}

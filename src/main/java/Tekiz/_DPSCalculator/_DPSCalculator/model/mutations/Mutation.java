package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.Keyable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

/**
 * Represents a mutation that a player character might have.
 * @param id An identifier if the object has been retrieved from a database.
 *           This is not required if object has been stored in a JSON file.
 * @param name The name of the mutation.
 * @param description The description of the perks effects.
 * @param positiveEffects Stores the positive effects a mutation has.
 * @param negativeEffects Stores the negative effects a mutation has.
 */
public record Mutation(@JsonProperty("id") int id,
					   @JsonProperty("name") String name,
					   @JsonProperty("description") String description,
					   @JsonProperty("positiveEffects") MutationEffects positiveEffects,
					   @JsonProperty("negativeEffects") MutationEffects negativeEffects) implements Keyable
{
	/**
	 * A method that aggregates and returns all positive and negative {@link Mutation} {@link Modifier}s.
	 *
	 * @return A {@link HashMap} of {@link Modifier}s and condition values.
	 */
	//todo - something is causing this to break when the name is changed (getMutationModifiers)
	@JsonIgnore
	public HashMap<Modifier, Boolean> aggregateMutationEffects()
	{
		HashMap<Modifier, Boolean> mutationMap = new HashMap<>();
		if (negativeEffects != null)
		{
			mutationMap.put(negativeEffects, true);
		}
		if (positiveEffects != null)
		{
			mutationMap.put(positiveEffects, true);
		}
		return mutationMap;
	}
}

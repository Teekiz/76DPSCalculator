package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Represents a mutation that a player character might have. */

@Data
@AllArgsConstructor
public class Mutation
{
	//while a mutation may be a form of modifier, its role is more complex.
	private final String name;
	private final String description;
	/** Stores the positive effects a mutation has. **/
	private final MutationEffects positiveEffects;
	/** Stores the negative effects a mutation has. **/
	private final MutationEffects negativeEffects;
}

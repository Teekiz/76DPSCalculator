package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/** Represents a mutation that a player character might have. */

@Data
@AllArgsConstructor
public class Mutation implements Serializable
{
	//while a mutation may be a form of modifier, its role is more complex.
	private final String name;
	private final String description;
	/** Stores the positive effects a mutation has. **/
	private final MutationEffects positiveEffects;
	/** Stores the negative effects a mutation has. **/
	private final MutationEffects negativeEffects;
}

package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mutation
{
	//while a mutation may be a form of modifier, its role is more complex.
	private final String name;
	private final String description;
	private final MutationEffects positiveEffects;
	private final MutationEffects negativeEffects;
}

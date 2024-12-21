package Tekiz._DPSCalculator._DPSCalculator.model.mutations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing {@link Mutation}.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MutationDTO
{
	private String id;
	private String name;
	private String description;
}

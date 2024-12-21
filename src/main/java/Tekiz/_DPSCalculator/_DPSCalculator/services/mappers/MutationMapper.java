package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.MutationDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service used to convert {@link Mutation} objects into {@link MutationDTO}.
 */
@Service
public class MutationMapper
{
	/**
	 * A method to convert a single mutation into a data transfer object (DTO).
	 * @param mutation The {@link Mutation} to be converted.
	 * @return The {@link Mutation} represented as a DTO ({@link MutationDTO}) or null if the mutation is null.
	 */
	public MutationDTO convertToDTO(Mutation mutation)
	{
		if (mutation == null) {return null;}
		return new MutationDTO(mutation.id(), mutation.name(),
			mutation.description());
	}

	/**
	 * A method to convert all mutations in a HashMap to a data transfer object (DTO) representation.
	 * @param mutations A {@link HashMap} of {@link Mutation} to be converted.
	 * @return A {@link Set} of {@link Mutation} represented as a DTOs ({@link MutationDTO}).
	 */
	public List<MutationDTO> convertAllToDTO(Set<Mutation> mutations)
	{
		return mutations.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	/**
	 * A method to convert all mutations in a list to a data transfer object (DTO) representation.
	 * @param mutations A {@link List} of {@link Mutation} to be converted.
	 * @return A {@link List} of {@link Mutation} represented as a DTOs ({@link MutationDTO}).
	 */
	public List<MutationDTO> convertAllToDTO(List<Mutation> mutations){
		return mutations.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
}

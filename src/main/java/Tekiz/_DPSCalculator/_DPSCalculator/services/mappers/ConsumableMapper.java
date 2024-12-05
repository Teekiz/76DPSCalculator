package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.*;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service used to convert {@link Consumable} objects into {@link ConsumableDTO}.
 */
@Service
public class ConsumableMapper
{
	/**
	 * A method to convert a single consumable into a data transfer object (DTO).
	 * @param consumable The {@link Consumable} to be converted.
	 * @return The {@link Consumable} represented as a DTO ({@link ConsumableDTO}) or null if the consumable is null.
	 */
	public ConsumableDTO convertToDTO(Consumable consumable)
	{
		if (consumable == null) {return null;}
		return new ConsumableDTO(consumable.id(), consumable.name(),
			consumable.consumableType().toString(), consumable.addictionType().toString());
	}

	/**
	 * A method to convert all consumables in a HashMap to a data transfer object (DTO) representation.
	 * @param consumables A {@link HashMap} of {@link Consumable} to be converted.
	 * @return A {@link List} of {@link Consumable} represented as a DTOs ({@link ConsumableDTO}).
	 */
	public List<ConsumableDTO> convertAllToDTO(HashMap<Consumable, Boolean> consumables)
	{
		return consumables.keySet().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}

	/**
	 * A method to convert all consumables in a list to a data transfer object (DTO) representation.
	 * @param consumables A {@link List} of {@link Consumable} to be converted.
	 * @return A {@link List} of {@link Consumable} represented as a DTOs ({@link ConsumableDTO}).
	 */
	public List<ConsumableDTO> convertAllToDTO(List<Consumable> consumables){
		return consumables.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
}

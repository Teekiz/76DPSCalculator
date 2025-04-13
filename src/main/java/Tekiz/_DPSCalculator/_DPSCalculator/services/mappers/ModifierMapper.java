package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ModifierMapper
{
	/**
	 * A method to convert a single modifier entry into a data transfer object (DTO).
	 * @param entry The {@link Map.Entry} to be converted.
	 * @return The {@link Map.Entry} represented as a DTO ({@link ModifierDTO}).
	 */
	public ModifierDTO<?> convertToModifierDTO(Map.Entry<ModifierTypes, ModifierValue<?>> entry){
		return new ModifierDTO<>(entry.getKey(), entry.getValue());
	}

	/**
	 * A method to convert all modifiers in a list to a data transfer object (DTO) representation.
	 * @param modifiers A {@link HashMap} of modifiers to be converted.
	 * @return A {@link List} of modifiers represented as a DTOs ({@link ModifierDTO}).
	 */
	public List<ModifierDTO<?>> convertAllModifiersToDTO(HashMap<ModifierTypes, ModifierValue<?>> modifiers){
		return modifiers.entrySet().stream()
			.map(this::convertToModifierDTO)
			.collect(Collectors.toList());
	}
}

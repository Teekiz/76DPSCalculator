package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.Modification;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlotDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link WeaponMod} or {@link ArmourMod} objects to a {@link ModificationDTO} object.
 */
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ModificationMapper
{
	private final ModifierMapper modifierMapper;

	/**
	 * A method to convert a single modificationSlot into a data transfer object (DTO).
	 * @param modificationSlot The {@link ModificationSlot} to be converted.
	 * @return The {@link ModificationSlot} represented as a DTO ({@link ModificationSlotDTO}).
	 */
	public ModificationSlotDTO convertToModificationSlotDTO(ModificationSlot<?> modificationSlot){
		if (modificationSlot==null){return null;}

		return new ModificationSlotDTO(convertToModificationDTO(modificationSlot.getCurrentModification()), modificationSlot.isCanSlotBeChanged());
	}

	/**
	 * A method to convert all modifications slots in a list to a data transfer object (DTO) representation.
	 * @param modificationSlots A {@link HashMap} of {@link ModType} and {@link ModificationSlot} to be converted.
	 * @return A {@link HashMap} of {@link ModType} and {@link ModificationSlot} represented as a DTOs ({@link ModificationSlotDTO}).
	 */
	public HashMap<ModType, ModificationSlotDTO> convertHashMapToModificationDTO(HashMap<ModType, ? extends ModificationSlot<? extends Modification>> modificationSlots)
	{
		return modificationSlots.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> convertToModificationSlotDTO(entry.getValue()),
				(existing, _) -> existing,
				HashMap::new
			));
	}

	/**
	 * A method to convert a single modification into a data transfer object (DTO).
	 * @param modification The {@link Modification} to be converted.
	 * @return The {@link WeaponMod} represented as a DTO ({@link ModificationDTO}).
	 */
	public ModificationDTO convertToModificationDTO(Modification modification){
		if (modification==null){return null;}

		return new ModificationDTO(modification.id(), modification.name(), modification.modType(), modifierMapper.convertAllModifiersToDTO(modification.effects()));
	}

	/**
	 * A method to convert all modifications in a list to a data transfer object (DTO) representation.
	 * @param modifications A {@link List} of {@link Modification} to be converted.
	 * @return A {@link List} of {@link Modification} represented as a DTOs ({@link ModificationDTO}).
	 */
	public List<ModificationDTO> convertListToModificationDTO(List<? extends Modification> modifications)
	{
		return modifications.stream()
			.map(this::convertToModificationDTO)
			.collect(Collectors.toList());
	}
}

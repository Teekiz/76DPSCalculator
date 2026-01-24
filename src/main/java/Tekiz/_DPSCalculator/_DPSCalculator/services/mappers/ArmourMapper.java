package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourNameDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link Armour} object to a {@link ArmourDTO} object.
 */
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ArmourMapper
{
	/**
	 * Gets all the equipped armour in a loadout and converts them to a list of data transfer object (DTO).
	 * @param equippedArmour The equipped armour in a loadout.
	 * @return A {@link List} of {@link ArmourDTO}.
	 */
	public List<ArmourDTO> convertEquippedArmour(EquippedArmour equippedArmour){
		List<ArmourDTO> armourDTOS = new ArrayList<>();

		if (equippedArmour.getEquippedUnderArmour() != null){
			armourDTOS.add(convertToDetailsDTO(equippedArmour.getEquippedUnderArmour()));
		}

		equippedArmour.getEquippedOverArmourPieces()
			.stream()
			.map(this::convertToDetailsDTO)
			.forEach(armourDTOS::add);
		equippedArmour.getEquippedPowerArmourPieces()
			.stream()
			.map(this::convertToDetailsDTO)
			.forEach(armourDTOS::add);
		return armourDTOS;
	}
	/**
	 * A method to convert a single armour piece into a data transfer object (DTO).
	 * @param armour The {@link Armour} to be converted.
	 * @return The {@link Armour} represented as a DTO ({@link ArmourNameDTO}) or null if the armour piece is null.
	 */
	public ArmourNameDTO convertToNameDTO(Armour armour)
	{
		if (armour == null) {return null;}
		return ArmourNameDTO.builder()
			.id(armour.getId())
			.name(armour.getName())
			.armourType(armour.getArmourType())
			.armourPiece(armour.getArmourPiece())
			.build();
	}

	/**
	 * A method to convert all armour pieces in a list to a data transfer object (DTO) representation.
	 * @param armour A {@link List} of {@link Armour} to be converted.
	 * @return A {@link List} of {@link Armour} represented as a DTOs ({@link ArmourNameDTO}).
	 */
	public List<ArmourNameDTO> convertAllToNameDTO(List<Armour> armour)
	{
		return armour.stream()
			.map(this::convertToNameDTO)
			.collect(Collectors.toList());
	}

	/**
	 * A method to convert a single armour piece into a data transfer object (DTO).
	 * @param armour The {@link Armour} to be converted.
	 * @return The {@link Armour} represented as a DTO ({@link ArmourDTO}) or null if the weapon is null.
	 */
	public ArmourDTO convertToDetailsDTO(Armour armour)
	{
		if (armour==null){return null;}
		return ArmourDTO.builder()
			.id(armour.getId())
			.name(armour.getName())
			.armourType(armour.getArmourType())
			.armourPiece(armour.getArmourPiece())
			.armourLevel(armour.getArmourLevel())
			.armourSlot(armour.getArmourSlot())
			.armourResistance(armour.getArmourResistance())
			.modifications(armour.getModifications())
			.build();
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.*;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service used to convert {@link DPSDetails} objects into {@link DPSDetailsDTO}.
 */
@Service
public class DPSDetailsMapper
{
	/**
	 * A method used to convert a {@link DPSDetails} into a {@link DPSDetailsDTO}.
	 * @param dpsDetails The dps details to convert.
	 * @return A dpsDetails DTO.
	 */
	public DPSDetailsDTO convertToDTO(DPSDetails dpsDetails)
	{
		return new DPSDetailsDTO(
			dpsDetails.getLoadoutID(),
			dpsDetails.getWeaponName(),
			dpsDetails.getModifiersUsed().stream().map(detail -> new ModifierDetailsDTO(detail.modifierName(),
				detail.modifierType().getDisplayName(), detail.modifierValue())).collect(Collectors.toSet()),
			dpsDetails.getShotsPerSecond(),
			dpsDetails.getTimeToEmptyMagazine(),
			convertDpsDetailsToDTO(dpsDetails.getDamageDetailsRecords()),
			dpsDetails.getTotalDamagePerShot(),
			dpsDetails.getTotalDamagePerSecond(),
			dpsDetails.getTimeToConsumeActionPoints(),
			dpsDetails.getShotsRequiredToFillCriticalMeter());
	}

	/**
	 * A method to convert all DPSDetails in a list to a data transfer object (DTO) representation.
	 * @param dpsDetailsList A {@link List} of {@link DPSDetails} to be converted.
	 * @return A {@link List} of {@link DPSDetails} represented as a DTOs ({@link DPSDetailsDTO}).
	 */
	public List<DPSDetailsDTO> convertAllToDTO(List<DPSDetails> dpsDetailsList){
		return dpsDetailsList.stream().
			map(this::convertToDTO)
			.toList();
	}

	public HashMap<String, DamageDetails> convertDpsDetailsToDTO(Map<DamageType, DamageDetails> dpsDetails){
		HashMap<String, DamageDetails> dtoMap = new HashMap<>();
		dpsDetails.forEach((key, value) -> dtoMap.put(key.name(), value));
		return dtoMap;
	}
}

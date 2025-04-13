package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.*;
import java.util.List;
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
			dpsDetails.getModifiersUsed(),
			dpsDetails.getShotsPerSecond(),
			dpsDetails.getTimeToEmptyMagazine(),
			dpsDetails.getDamageDetailsRecords(),
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
}

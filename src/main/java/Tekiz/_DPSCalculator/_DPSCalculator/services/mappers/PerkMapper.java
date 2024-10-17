package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkDTO;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link Perk} object to a {@link PerkDTO} object.
 */
@Service
public class PerkMapper
{
	/**
	 * A method to convert a single perk into a data transfer object (DTO).
	 * @param perk The {@link Perk} to be converted.
	 * @return The {@link Perk} represented as a DTO ({@link PerkDTO}) or null if the perk is null.
	 */
	public PerkDTO convertToDTO(Perk perk)
	{
		if (perk == null) {return null;}
		return new PerkDTO(perk.id(), perk.name(), perk.perkRank().getCurrentRank(),
			perk.perkRank().getBaseCost(), perk.perkRank().getMaxRank(), perk.description());
	}

	/**
	 * A method to convert all perks in a HashMap to a data transfer object (DTO) representation.
	 * @param perks A {@link List} of {@link Perk} to be converted.
	 * @return A {@link List} of {@link Perk} represented as a DTOs ({@link PerkDTO}).
	 */
	public List<PerkDTO> convertAllToDTO(HashMap<Perk, Boolean> perks)
	{
		return perks.keySet().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
}

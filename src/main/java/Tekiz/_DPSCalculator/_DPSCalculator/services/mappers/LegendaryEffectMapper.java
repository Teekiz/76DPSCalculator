package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.Modification;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlotDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectSlotDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service to convert legendary effects into data transfer objects (DTOs).
 */
@Service
public class LegendaryEffectMapper
{
	/**
	 * A method to convert a single legendaryEffectSlot into a data transfer object (DTO).
	 * @param legendaryEffectSlot The {@link LegendaryEffectSlot} to be converted.
	 * @return The {@link LegendaryEffectSlot} represented as a DTO ({@link LegendaryEffectSlotDTO}).
	 */
	public LegendaryEffectSlotDTO convertToLegendaryEffectSlotDTO(LegendaryEffectSlot legendaryEffectSlot){
		if (legendaryEffectSlot==null){return null;}

		return new LegendaryEffectSlotDTO(convertToDTO(legendaryEffectSlot.getCurrentLegendaryEffect()), legendaryEffectSlot.isCanSlotBeChanged());
	}

	/**
	 * A method to convert all legendary effect slots in a list to a data transfer object (DTO) representation.
	 * @param legendaryEffectSlots A {@link HashMap} of {@link StarType} and {@link LegendaryEffectSlot} to be converted.
	 * @return A {@link HashMap} of {@link StarType} and {@link LegendaryEffectSlot} represented as a DTOs ({@link LegendaryEffectSlotDTO}).
	 */
	public HashMap<StarType, LegendaryEffectSlotDTO> convertHashMapToLegendaryEffectDTO(HashMap<StarType, LegendaryEffectSlot> legendaryEffectSlots)
	{
		return legendaryEffectSlots.entrySet()
			.stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> convertToLegendaryEffectSlotDTO(entry.getValue()),
				(existing, _) -> existing,
				HashMap::new
			));
	}

	/**
	 * A method to convert a single legendary effect into a data transfer object (DTO).
	 * @param legendaryEffect The {@link LegendaryEffect} to be converted.
	 * @return The {@link LegendaryEffect} represented as a DTO ({@link LegendaryEffectDTO}) or null if the legendary effect is null.
	 */
	public LegendaryEffectDTO convertToDTO(LegendaryEffect legendaryEffect)
	{
		if (legendaryEffect == null) {return null;}
		return new LegendaryEffectDTO(legendaryEffect.id(), legendaryEffect.name(),
			legendaryEffect.description(), legendaryEffect.isHidden(), legendaryEffect.categories(), legendaryEffect.starType());
	}

	/**
	 * A method to convert all legendary effect in a list to a data transfer object (DTO) representation.
	 * @param legendaryEffects effect A {@link List} of {@link LegendaryEffect} to be converted.
	 * @return A {@link List} of {@link LegendaryEffect} represented as a DTOs ({@link LegendaryEffectDTO}).
	 */
	public List<LegendaryEffectDTO> convertAllToDTO(List<LegendaryEffect> legendaryEffects){
		return legendaryEffects.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectObject;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service to convert legendary effects into data transfer objects (DTOs).
 */
@Service
public class LegendaryEffectMapper
{
	/**
	 * A method to convert a single legendary effect into a data transfer object (DTO).
	 * @param legendaryEffect The {@link LegendaryEffect} to be converted.
	 * @return The {@link LegendaryEffect} represented as a DTO ({@link LegendaryEffectDTO}) or null if the legendary effect is null.
	 */
	public LegendaryEffectDTO convertToDTO(LegendaryEffect legendaryEffect)
	{
		if (legendaryEffect == null) {return null;}
		return new LegendaryEffectDTO(legendaryEffect.id(), legendaryEffect.name(),
			legendaryEffect.description(), legendaryEffect.categories(), legendaryEffect.starType());
	}

	/**
	 * A method to convert all legendary effect in a HashMap to a data transfer object (DTO) representation.
	 * @param legendaryEffectObject An object containing legendary effects to be converted.
	 * @return A {@link List} of {@link LegendaryEffect} represented as a DTOs ({@link LegendaryEffectDTO}).
	 */
	public List<LegendaryEffectDTO> convertAllToDTO(LegendaryEffectObject legendaryEffectObject)
	{
		return legendaryEffectObject.getLegendaryEffects().keySet().stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
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

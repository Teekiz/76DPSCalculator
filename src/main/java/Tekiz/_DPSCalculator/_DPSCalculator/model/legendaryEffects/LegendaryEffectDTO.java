package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing legendary effects.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LegendaryEffectDTO
{
	String id;
	String name;
	String description;
	List<Category> categories;
	StarType starType;
}

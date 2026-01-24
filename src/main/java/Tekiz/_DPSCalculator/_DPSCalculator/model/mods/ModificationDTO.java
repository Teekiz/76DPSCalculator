package Tekiz._DPSCalculator._DPSCalculator.model.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing modifications.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModificationDTO
{
	private String id;
	private String name;
	private ModType modType;
	private List<ModifierDTO<?>> modificationEffects;
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class ArmourMod
{
	private String modName;
	private ArmourPiece armourPiece;
}

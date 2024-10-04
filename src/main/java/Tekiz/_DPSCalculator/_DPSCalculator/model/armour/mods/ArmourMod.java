package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic modification that can be made to a piece of armour. Each armour contains a {@code modName}
 * and a {@code armourPiece}. The armour piece is the body part a mod can be applied to.
 */
@Data
@AllArgsConstructor
public abstract class ArmourMod implements Serializable
{
	private String modName;
	private ArmourPiece armourPiece;
}

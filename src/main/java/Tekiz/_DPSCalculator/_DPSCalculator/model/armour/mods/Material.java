package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import java.util.HashMap;
import lombok.Getter;

/**
 * Represents a material modification that can be made to a piece of armour.
 * Each {@link Material} contains the bonus resistances and effect the mod may have.
 */

@Getter
public class Material extends ArmourMod
{
	private final HashMap<Integer, ArmourResistance> materialResistanceBonus;
	private final String materialEffect;

	public Material(String modName, ArmourPiece armourPiece, HashMap<Integer, ArmourResistance> materialResistanceBonus, String materialEffect)
	{
		super(modName, armourPiece);
		this.materialResistanceBonus = materialResistanceBonus;
		this.materialEffect = materialEffect;
	}
}

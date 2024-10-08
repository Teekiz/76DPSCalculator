package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import lombok.Getter;

/**
 * Represents a miscellaneous modification that can be made to a piece of armour.
 * Each {@link Material} effect the mod may have.
 */
@Getter
public class Miscellaneous extends ArmourMod
{
	private final String modEffect;

	public Miscellaneous(String modName, ArmourPiece armourPiece, String modEffect)
	{
		super(modName, armourPiece);
		this.modEffect = modEffect;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Armour.ArmourPiece;
import lombok.Getter;

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

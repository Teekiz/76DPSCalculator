package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Armour
{
	private String armourName;
	private int armourLevel;
	private ArmourType armourType;
	private ArmourPiece armourPiece;
	private ArmourSet armourSet;
	private HashMap<Integer, ArmourResistance> armourResistance;

	//mods
	private Material armourMaterial;
	private Miscellaneous armourMisc;

	//todo - add check to ensure piece matches
	//todo - consider changing from data to get only
	public void setMod(ArmourMod armourMod)
	{
		switch (armourMod)
		{
			case Material material -> this.setArmourMaterial(material);
			case Miscellaneous miscellaneous -> this.setArmourMisc(miscellaneous);
			default -> {}
		}
	}
}

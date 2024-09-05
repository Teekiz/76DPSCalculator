package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.ArmourType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArmourResistance
{
	private int damageResistance;
	private int energyResistance;
	private int radiationResistance;
	private int cryoResistance;
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourClassification;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing armour (limited data only).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class ArmourDTO extends ArmourNameDTO
{
	private int armourLevel;
	private ArmourSlot armourSlot;
	private String armourSet;
	private HashMap<Integer, ArmourResistance> armourResistance;
	/** if the armour classification applies. */
	private ArmourClassification armourClassification;
	private HashMap<ModType, ModificationSlot<ArmourMod>> modifications;
}

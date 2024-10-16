package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourResistance;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a material modification that can be made to a piece of armour.
 * Each {@link Material} contains the bonus resistances and effect the mod may have.
 */

@Value
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Material extends ArmourMod
{
	@JsonProperty("armourResistance")
	HashMap<Integer, ArmourResistance> materialResistanceBonus;
	@JsonProperty("materialEffect")
	String materialEffect;
}

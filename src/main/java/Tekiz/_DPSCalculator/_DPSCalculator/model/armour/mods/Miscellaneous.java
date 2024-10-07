package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a miscellaneous modification that can be made to a piece of armour.
 * Each {@link Material} effect the mod may have.
 */
@Value
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Miscellaneous extends ArmourMod
{
	@JsonProperty("modEffect")
	String modEffect;
}

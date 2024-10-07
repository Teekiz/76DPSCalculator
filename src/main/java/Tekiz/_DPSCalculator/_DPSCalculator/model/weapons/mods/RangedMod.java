package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a generic modification that can be made to a weapon.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class RangedMod implements Serializable
{
	@JsonProperty("name")
	private final String name;

}

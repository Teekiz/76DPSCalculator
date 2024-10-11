package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a generic modification that can be made to a weapon.
 */
@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public abstract class RangedMod implements Serializable
{
	@JsonProperty("name")
	private final String name;

}

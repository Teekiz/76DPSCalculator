package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a generic modification that can be made to a weapon.
 */
@Data
@AllArgsConstructor
public abstract class RangedMod implements Serializable
{
	private String name;
}

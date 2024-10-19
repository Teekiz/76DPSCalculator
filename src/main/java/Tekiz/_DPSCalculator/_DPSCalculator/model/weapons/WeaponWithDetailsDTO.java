package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing weapons (limited data only).
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeaponWithDetailsDTO
{
	private String weaponName;
	private String weaponType;
	private String damageType;
	private HashMap<Integer, Double> weaponDamageByLevel;
}

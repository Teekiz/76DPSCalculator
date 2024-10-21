package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weapons (limited data only).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class WeaponDetailsDTO
{
	private int weaponID;
	private String weaponName;
	private String weaponType;
	private String damageType;
	private HashMap<Integer, Double> weaponDamageByLevel;
	private int apCost;
}

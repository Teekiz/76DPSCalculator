package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing {@link DPSDetails}.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DPSDetailsDTO
{
	private int loadoutID;
	private String weaponName;
	private Set<ModifierDetails> modifierDetails;

	private double shotsPerSecond;
	private double timeToEmptyMagazine;

	private HashMap<DamageType, DamageDetails> damageDetailsRecords;

	private double totalDamagePerShot;
	private double totalDamagePerSecond;

	double timeToConsumeActionPoints;
	double shotsRequiredToFillCriticalMeter;
}

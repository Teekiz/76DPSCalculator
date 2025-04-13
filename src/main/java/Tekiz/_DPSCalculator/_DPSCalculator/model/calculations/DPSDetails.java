package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DPSDetails
{
	final int loadoutID;
	String weaponName;
	final Set<ModifierDetails> modifiersUsed = new HashSet<>();

	double shotsPerSecond;
	double timeToEmptyMagazine;

	final HashMap<DamageType, DamageDetails> damageDetailsRecords = new HashMap<>();

	double timeToConsumeActionPoints;
	double shotsRequiredToFillCriticalMeter;

	/**
	 * A method used to return the details of a damage record. A new record is created if an existing record cannot be found.
	 * @param damageType The type of damage this record will be used to store.
	 * @return A {@link DamageDetails} record.
	 */
	public DamageDetails getDamageDetailsRecord(DamageType damageType){
		if (damageType == null) {
			damageType = DamageType.UNKNOWN;
		}
		return damageDetailsRecords.computeIfAbsent(damageType, k -> new DamageDetails());
	}

	public double getTotalDamagePerShot()
	{
		return damageDetailsRecords.values().stream().mapToDouble(DamageDetails::getDamagePerShot).sum();
	}

	public double getTotalDamagePerSecond()
	{
		return damageDetailsRecords.values().stream().mapToDouble(DamageDetails::getDamagePerSecond).sum();
	}
}

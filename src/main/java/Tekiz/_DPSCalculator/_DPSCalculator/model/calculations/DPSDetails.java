package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	final List<ModifierDetails> modifiersUsed = new ArrayList<>();

	double shotsPerSecond;
	double timeToEmptyMagazine;

	//damage before factoring in reload time or swing speed.
	final HashMap<DamageType, Double> damagePerShot = new HashMap<>();
	//damage after factoring in reload time or swing speed.
	final HashMap<DamageType, Double> damagePerSecond = new HashMap<>();

	double bodyPartMultiplier;
	double damageResistMultiplier;

	//VATS
	//todo
	double damagePerActionPoint;
	double timeToConsumeActionPoints;
	double shotsRequiredToFillCriticalMeter;
	final HashMap<DamageType, Double> criticalDamagePerShot  = new HashMap<>();

	public double getTotalDamagePerShot()
	{
		return damagePerShot.values().stream().mapToDouble(Double::doubleValue).sum();
	}

	public double getTotalDamagePerSecond()
	{
		return damagePerSecond.values().stream().mapToDouble(Double::doubleValue).sum();
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DamageCalculationService
{
	/*
		Damage is calculated by:
		Damage = OutgoingDamage * DamageResistMultiplier * BodyPartMultiplier

		OutgoingDamage = Base * (1 + DamageBonus) * DamageMultiplier1 * DamageMultiplier2 *...
		DamageBonus comes for consumables and perks.
	 */
	//todo - update getBaseDamage
	public double calculateOutgoingDamage(double baseDamage, List<Double> additiveDamage, List<Double> multiplicativeDamage)
	{
		return 0.0;
	}
}

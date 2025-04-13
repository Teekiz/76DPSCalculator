package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DamageDetails
{
	//the actual base damage
	double baseDamage;
	double baseDamageBonuses;

	double bonusDamageMultiplier;
	double multiplicativeDamageMultiplier;
	double damageSneakBonuses;

	double criticalDamagePerShot;
	double damageCriticalBonuses;

	double damagePerShot;
	double damagePerSecond;

	double bodyPartMultiplier;
	double damageResistMultiplier;

	public void setBaseDamageAndBonuses(double baseDamage, double baseDamageBonuses){
		this.baseDamage = baseDamage;
		this.baseDamageBonuses = baseDamageBonuses;
	}
}

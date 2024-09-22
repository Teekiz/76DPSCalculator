package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DamageCalculationService
{
	@Autowired
	private final BaseDamageService baseDamageService;
	@Autowired
	private final BonusDamageService bonusDamageService;
	@Autowired
	public DamageCalculationService(BaseDamageService baseDamageService, BonusDamageService bonusDamageService)
	{
		this.baseDamageService = baseDamageService;
		this.bonusDamageService = bonusDamageService;
	}

	/*
		Damage is calculated by:
		Damage = OutgoingDamage * DamageResistMultiplier * BodyPartMultiplier

		OutgoingDamage = Base * (1 + DamageBonus) * DamageMultiplier1 * DamageMultiplier2 *...
		DamageBonus comes for consumables and perks.

		Order of operations:
		weapons, enemy and environment
		perks affect mutations, consumables and stats
		mutations affect consumables and stats
		consumables affect stats
		legendary effects
		stats last
	 */
	//todo - update getBaseDamage
	public double calculateOutgoingDamage(Loadout loadout)
	{
		double baseDamage = baseDamageService.calculateBaseDamage(loadout);
		double bonusDamage = bonusDamageService.calculateBonusDamage(loadout);

		double outgoingDamage = baseDamage * bonusDamage;
		return round(outgoingDamage);
	}

	public double round(double value)
	{
		BigDecimal bigDecimal = BigDecimal.valueOf(value);
		return bigDecimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}

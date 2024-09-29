package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A calculation service to find out the damage output of a loadout.
 */
@Service
public class DamageCalculationService
{
	private final BaseDamageService baseDamageService;
	private final BonusDamageService bonusDamageService;
	private final DamageMultiplierService damageMultiplierService;
	private final ModifierBoostService boostService;


	/**
	 * The constructor for {@link DamageCalculationService}.
	 * @param baseDamageService A service that calculates the base damage from a loadout.
	 * @param bonusDamageService A service that calculates the bonus (additive) damage from a loadout.
	 * @param damageMultiplierService A service that calculates the multiplicative damage from a loadout.
	 * @param boostService A service that is used to store and apply boosts to {@link Modifier}'s values.
	 */
	@Autowired
	public DamageCalculationService(BaseDamageService baseDamageService, BonusDamageService bonusDamageService, DamageMultiplierService damageMultiplierService, ModifierBoostService boostService)
	{
		this.baseDamageService = baseDamageService;
		this.bonusDamageService = bonusDamageService;
		this.damageMultiplierService = damageMultiplierService;
		this.boostService = boostService;
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

	/**
	 * A method that calculates the total damage output of a given loadout.
	 * @param loadout The {@code loadout} being used to determine the damage output.
	 * @return A {@link Double} value of the loadout's damage output.
	 */
	public double calculateOutgoingDamage(Loadout loadout)
	{
		double baseDamage = baseDamageService.calculateBaseDamage(loadout);
		double bonusDamage = bonusDamageService.calculateBonusDamage(loadout);

		double totalDamage = damageMultiplierService.calculateMultiplicativeDamage(loadout,baseDamage * bonusDamage);

		boostService.clearBoosts();
		return round(totalDamage);
	}

	/**
	 * A method that rounds a {@link Double} value to a set number of decimal places.
	 * @param value The value that will be rounded.
	 * @return The rounded value.
	 */
	public double round(double value)
	{
		BigDecimal bigDecimal = BigDecimal.valueOf(value);
		return bigDecimal.setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}

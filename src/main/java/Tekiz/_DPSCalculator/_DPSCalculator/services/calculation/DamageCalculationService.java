package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;

//@Service
public class DamageCalculationService
{
	/*
		Damage is calculated by:
		Damage = OutgoingDamage * DamageResistMultiplier * BodyPartMultiplier

		OutgoingDamage = Base * (1 + DamageBonus) * DamageMultiplier1 * DamageMultiplier2 *...
		DamageBonus comes for consumables and perks.
	 */

	private final Player player;
	private final Weapon weapon;

	//@Autowired
	public DamageCalculationService(Player player, Weapon weapon)
	{
		this.player = player;
		this.weapon = weapon;
	}

	//todo - update getBaseDamage
	public double calculateOutgoingDamage()
	{
		double baseDamage = weapon.getBaseDamage(45);
		return 0.0;
	}
}

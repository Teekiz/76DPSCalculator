package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.Special;
import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.BonusTypes;
import lombok.Data;

@Data
public class Modifiers
{
	//todo - consider only refreshing affected data (e.g. damage, accuracy)
	private Special specialModifier;
	private WeaponModifier weaponModifier;
	private SneakAndCriticalModifier sneakAndCriticalModifier;

	public Modifiers()
	{
		specialModifier = new Special(0,0,0,0,0,0,0,0,99);
		weaponModifier = new WeaponModifier(0.0, 0.0, 1.0, 1.0,1.0,1.0,1.0,
			1.0,0, 0);
		sneakAndCriticalModifier = new SneakAndCriticalModifier(1, 1);
	}

	public void addModifier(BonusTypes bonusType, double bonus)
	{
		switch (bonusType)
		{
			case DAMAGE_ADDITIVE -> weaponModifier.addAdditiveWeaponDamageBonus(bonus);
			case DAMAGE_MULTIPLICATIVE -> weaponModifier.addMultiplicativeWeaponDamageBonus(bonus);
			case PENETRATION_PHYSICAL -> weaponModifier.addPenetrationPhysicalBonus(bonus);
			case PENETRATION_ENERGY -> weaponModifier.addPenetrationEnergyBonus(bonus);
			case PENETRATION_RADIATION -> weaponModifier.addPenetrationRadiationBonus(bonus);
			case PENETRATION_POISON -> weaponModifier.addPenetrationPoisonBonus(bonus);
			case ATTACKSPEED -> weaponModifier.addAttackSpeedBonus(bonus);
			case RELOADSPEED -> weaponModifier.addReloadSpeedBonus(bonus);
			case ACCURACY -> weaponModifier.addAccuracyBonus(bonus);
			case RANGE ->  weaponModifier.addRangeBonus(bonus);
			case SNEAK -> sneakAndCriticalModifier.addSneakBonus(bonus);
			case CRITICAL -> sneakAndCriticalModifier.addCriticalBonus(bonus);
		}
	}

	public void addModifier(BonusTypes bonusType, Specials special, int bonus)
	{
		if (bonusType == BonusTypes.SPECIAL)
		{
			specialModifier.modifySpecial(special, bonus);
		}
	}

}

package Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.OverArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.PowerArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import lombok.Getter;

@Getter
public enum Category
{
	MELEE_WEAPONS(MeleeWeapon.class),
	RANGED_WEAPONS(RangedWeapon.class),
	ARMOUR(OverArmourPiece.class),
	POWER_ARMOUR(PowerArmourPiece.class);

	private final Class<?> classType;

	Category(Class<?> classType){
		this.classType = classType;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public abstract class Weapon
{
	//todo - consider changing to add armour penetration and removing projectile amount
	protected final String weaponName;
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected final Map<Integer, Double> weaponDamageByLevel;
	protected int apCost;

	@Autowired
	protected final ModLoaderService modLoaderService;

	//todo change this - receiver damage is additive
	public abstract double getBaseDamage(int damage);
}

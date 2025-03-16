package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class WeaponModelTest extends BaseTestClass
{
	@Test
	public void testWeaponBaseDamage()
	{
		HashMap<Integer, List<WeaponDamage>> damageMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList_5 = new ArrayList<>();
		weaponDamageList_5.add(new WeaponDamage(DamageType.PHYSICAL, 20.0, 0));

		List<WeaponDamage> weaponDamageList_10 = new ArrayList<>();
		weaponDamageList_10.add(new WeaponDamage(DamageType.PHYSICAL, 30.0, 0));

		List<WeaponDamage> weaponDamageList_15 = new ArrayList<>();
		weaponDamageList_15.add(new WeaponDamage(DamageType.PHYSICAL, 40.0, 0));

		damageMap.put(5, weaponDamageList_5);
		damageMap.put(10, weaponDamageList_10);
		damageMap.put(15, weaponDamageList_15);

		Weapon weapon = RangedWeapon.builder()
			.id("TEST")
			.name("TEST")
			.weaponType(WeaponType.PISTOL)
			.weaponDamageByLevel(damageMap)
			.build();

		assertEquals(weapon.getBaseDamage(5).getFirst().damage(), 20.0);
		assertEquals(weapon.getBaseDamage(10).getFirst().damage(), 30.0);
		assertEquals(weapon.getBaseDamage(15).getFirst().damage(), 40.0);
	}
}

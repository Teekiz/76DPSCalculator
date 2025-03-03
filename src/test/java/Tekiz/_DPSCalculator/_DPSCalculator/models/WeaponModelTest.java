package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class WeaponModelTest extends BaseTestClass
{
	@Test
	public void testWeaponBaseDamage()
	{
		HashMap<Integer, Double> damageMap = new HashMap<>();
		damageMap.put(5, 20.0);
		damageMap.put(10, 30.0);
		damageMap.put(15, 40.0);

		Weapon weapon = RangedWeapon.builder()
			.id("TEST")
			.name("TEST")
			.weaponType(WeaponType.PISTOL)
			.weaponDamageByLevel(damageMap)
			.build();

		assertEquals(weapon.getBaseDamage(5), 20.0);
		assertEquals(weapon.getBaseDamage(10), 30.0);
		assertEquals(weapon.getBaseDamage(15), 40.0);
	}
}

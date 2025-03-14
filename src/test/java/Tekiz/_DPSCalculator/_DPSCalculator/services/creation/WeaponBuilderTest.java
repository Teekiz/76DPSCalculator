package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponBuilderTest extends BaseTestClass
{
	@Test
	void testPistolBuilderWithOptionalData() throws IOException
	{
		HashMap<Integer, Double> weaponDamageMap = new HashMap<>();
		weaponDamageMap.put(5, 15.0);
		weaponDamageMap.put(15, 18.0);
		weaponDamageMap.put(25, 21.0);
		weaponDamageMap.put(35, 24.0);
		weaponDamageMap.put(45, 28.0);

		RangedWeapon pistol = RangedWeapon.builder()
			.name("10mm Pistol")
			.weaponType(WeaponType.PISTOL)
			.damageType(DamageType.PHYSICAL)
			.weaponDamageByLevel(weaponDamageMap)
			.apCost(20)
			.attackSpeed(0.67)
			.criticalBonus(100)
			.build();
		assertNotNull(pistol);
		assertEquals("10mm Pistol", pistol.getName());
		assertEquals(21, pistol.getWeaponDamageByLevel().get(25));
		assertEquals(20, pistol.getApCost());
	}

	@Test
	void testMeleeWeaponBuilder() throws IOException
	{
		HashMap<Integer, Double> weaponDamageMap = new HashMap<>();
		weaponDamageMap.put(25, 28.0);
		weaponDamageMap.put(35, 33.0);
		weaponDamageMap.put(45, 40.0);
		MeleeWeapon blade = MeleeWeapon.builder()
			.name("Assaultron blade")
			.weaponType(WeaponType.ONEHANDED)
			.damageType(DamageType.PHYSICAL)
			.weaponDamageByLevel(weaponDamageMap)
			.apCost(35)
			.attackSpeed(1.14)
			.criticalBonus(100)
			.build();

		assertNotNull(blade);
		assertEquals("Assaultron blade", blade.getName());
		assertEquals(33, blade.getWeaponDamageByLevel().get(35));
		assertEquals(35, blade.getApCost());
	}
}

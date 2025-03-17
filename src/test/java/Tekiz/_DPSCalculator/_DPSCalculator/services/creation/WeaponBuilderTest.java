package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponBuilderTest extends BaseTestClass
{
	@Test
	void testPistolBuilderWithOptionalData() throws IOException
	{
		HashMap<Integer, List<WeaponDamage>> weaponDamageMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList_5 = new ArrayList<>();
		weaponDamageList_5.add(new WeaponDamage(DamageType.PHYSICAL, 15.0, 0));

		List<WeaponDamage> weaponDamageList_15 = new ArrayList<>();
		weaponDamageList_15.add(new WeaponDamage(DamageType.PHYSICAL, 18.0, 0));

		List<WeaponDamage> weaponDamageList_25 = new ArrayList<>();
		weaponDamageList_25.add(new WeaponDamage(DamageType.PHYSICAL, 21.0, 0));

		List<WeaponDamage> weaponDamageList_35 = new ArrayList<>();
		weaponDamageList_35.add(new WeaponDamage(DamageType.PHYSICAL, 24.0, 0));

		List<WeaponDamage> weaponDamageList_45 = new ArrayList<>();
		weaponDamageList_45.add(new WeaponDamage(DamageType.PHYSICAL, 28.0, 0));

		weaponDamageMap.put(5, weaponDamageList_5);
		weaponDamageMap.put(15, weaponDamageList_15);
		weaponDamageMap.put(25, weaponDamageList_25);
		weaponDamageMap.put(35, weaponDamageList_35);
		weaponDamageMap.put(45, weaponDamageList_45);


		RangedWeapon pistol = RangedWeapon.builder()
			.name("10mm Pistol")
			.weaponType(WeaponType.PISTOL)
			.weaponDamageByLevel(weaponDamageMap)
			.apCost(20)
			.criticalBonus(100)
			.build();
		assertNotNull(pistol);
		assertEquals("10mm Pistol", pistol.getName());
		assertEquals(21, pistol.getWeaponDamageByLevel().get(25).getFirst().damage());
		assertEquals(20, pistol.getApCost());
	}

	@Test
	void testMeleeWeaponBuilder() throws IOException
	{
		HashMap<Integer, List<WeaponDamage>> weaponDamageMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList_25 = new ArrayList<>();
		weaponDamageList_25.add(new WeaponDamage(DamageType.PHYSICAL, 28.0, 0));

		List<WeaponDamage> weaponDamageList_35 = new ArrayList<>();
		weaponDamageList_35.add(new WeaponDamage(DamageType.PHYSICAL, 33.0, 0));

		List<WeaponDamage> weaponDamageList_45 = new ArrayList<>();
		weaponDamageList_45.add(new WeaponDamage(DamageType.PHYSICAL, 40.0, 0));


		weaponDamageMap.put(25, weaponDamageList_25);
		weaponDamageMap.put(35, weaponDamageList_35);
		weaponDamageMap.put(45, weaponDamageList_45);

		MeleeWeapon blade = MeleeWeapon.builder()
			.name("Assaultron blade")
			.weaponType(WeaponType.ONEHANDED)
			.weaponDamageByLevel(weaponDamageMap)
			.apCost(35)
			.criticalBonus(100)
			.build();

		assertNotNull(blade);
		assertEquals("Assaultron blade", blade.getName());
		assertEquals(33, blade.getWeaponDamageByLevel().get(35).getFirst().damage());
		assertEquals(35, blade.getApCost());
	}
}

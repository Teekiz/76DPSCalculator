package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons.MeleeWeaponBuilder;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeaponBuilder;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.WeaponFactory;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WeaponBuilderTest
{
	@Autowired
	private WeaponFactory weaponFactory;

	@Test
	void testPistolBuilderWithOptionalData() throws IOException
	{
		HashMap<Integer, Double> weaponDamageMap = new HashMap<>();
		weaponDamageMap.put(5, 15.0);
		weaponDamageMap.put(15, 18.0);
		weaponDamageMap.put(25, 21.0);
		weaponDamageMap.put(35, 24.0);
		weaponDamageMap.put(45, 28.0);

		RangedWeapon pistol = new RangedWeaponBuilder("10mm Pistol", WeaponType.PISTOL, DamageType.BALLISTIC, weaponDamageMap, 20, 0.67, 100).setAccuracy(20).build();
		assertNotNull(pistol);
		assertEquals("10mm Pistol", pistol.getWeaponName());
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
		MeleeWeapon blade = new MeleeWeaponBuilder("Assaultron blade", WeaponType.ONEHANDED, DamageType.PHYSICAL, weaponDamageMap, 35, 1.14, 100).build();

		assertNotNull(blade);
		assertEquals("Assaultron blade", blade.getWeaponName());
		assertEquals(33, blade.getWeaponDamageByLevel().get(35));
		assertEquals(35, blade.getApCost());
	}
}

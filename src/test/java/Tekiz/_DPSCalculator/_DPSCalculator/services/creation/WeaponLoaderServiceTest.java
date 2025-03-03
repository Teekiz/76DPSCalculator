package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WeaponLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;
	@Autowired
	WeaponFactory weaponFactory;

	@Test
	void testGetWeapon() throws IOException
	{
		String weaponName = "10MMPISTOL";
		Weapon weapon = dataLoaderService.loadDataByName(weaponName, Weapon.class, weaponFactory);
		assertNotNull(weapon);
		assertEquals("Test 10mm pistol", weapon.getName());

		String meleeWeaponName = "ASSAULTRONBLADE";
		Weapon meleeWeapon = dataLoaderService.loadDataByName(meleeWeaponName, Weapon.class, weaponFactory);
		assertNotNull(meleeWeapon);
		assertEquals("Assaultron blade", meleeWeapon.getName());
	}

	@Test
	void testLoadAllWeapons() throws IOException
	{
		List<Weapon> weapons = dataLoaderService.loadAllData("weapons", Weapon.class, weaponFactory);
		assertNotNull(weapons);
		assertEquals("Test 10mm pistol", weapons.stream()
			.filter(weapon -> weapon.getName().equalsIgnoreCase("Test 10mm pistol"))
			.findFirst()
			.orElse(null)
			.getName());
	}
}
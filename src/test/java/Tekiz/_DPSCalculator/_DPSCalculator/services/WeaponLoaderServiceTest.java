package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json"})
public class WeaponLoaderServiceTest
{
	@Autowired
	private WeaponLoaderService weaponLoaderService;

	@Test
	void testGetWeapon() throws IOException
	{
		String weaponName = "10MMPISTOL";
		Weapon weapon = weaponLoaderService.getWeapon(weaponName);
		assertNotNull(weapon);
		assertEquals("Test 10mm pistol", weapon.getWeaponName());
	}

	@Test
	void testLoadAllWeapons() throws IOException
	{
		HashMap<String, String> weapons = weaponLoaderService.getAllWeaponsWithName();
		assertNotNull(weapons);
		assertTrue(weapons.containsKey("10MMPISTOL"));
		assertEquals("Test 10mm pistol", weapons.get("10MMPISTOL"));
	}
}
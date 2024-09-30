package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.WeaponLoaderService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
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

		String meleeWeaponName = "ASSAULTRONBLADE";
		Weapon meleeWeapon = weaponLoaderService.getWeapon(meleeWeaponName);
		assertNotNull(meleeWeapon);
		assertEquals("Assaultron blade", meleeWeapon.getWeaponName());
	}

	@Test
	void testLoadAllWeapons() throws IOException
	{
		List<Weapon> weapons = weaponLoaderService.getAllWeapons();
		assertNotNull(weapons);
		assertEquals("Test 10mm pistol", weapons.stream()
			.filter(weapon -> weapon.getWeaponName().equalsIgnoreCase("Test 10mm pistol"))
			.findFirst()
			.orElse(null)
			.getWeaponName());
	}
}
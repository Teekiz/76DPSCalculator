package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.Pistol;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.WeaponLoaderService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json", "perk.data.file.path=src/test/resources/data/perkData/testPerks.json", "receivers.data.file.path=src/test/resources/data/weaponData/testReceivers.json"})
public class PerkLogicTest
{
	@Autowired
	WeaponLoaderService weaponLoaderService;
	@Autowired
	PerkLoaderService perkLoaderService;
	@Autowired
	ModLoaderService modLoaderService;
	@Autowired
	PerkLogic perkLogic;

	@Test
	public void testFalseCondition() throws IOException
	{
		String weaponName = "10MMPISTOL";
		Weapon weapon = weaponLoaderService.getWeapon(weaponName);
		assertNotNull(weapon);
		assertEquals("Test 10mm pistol", weapon.getWeaponName());

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		//
		boolean check = perkLogic.evaluateCondition(perk, weapon);
		assertFalse(check);
	}

	@Test
	public void testPistolWithDifferentReceivers() throws IOException
	{
		String weaponName = "10MMPISTOL";
		Weapon weapon = weaponLoaderService.getWeapon(weaponName);
		assertNotNull(weapon);

		String perkName = "GUNSLINGER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		boolean check = perkLogic.evaluateCondition(perk, weapon);
		assertFalse(check);

		Pistol pistol = (Pistol) weapon;
		Receiver newReceiver = modLoaderService.getReceiver("CALIBRATE");
		pistol.setReceiver(newReceiver);

		boolean checkWithNewReceiver = perkLogic.evaluateCondition(perk, pistol);
		assertTrue(checkWithNewReceiver);
	}
}

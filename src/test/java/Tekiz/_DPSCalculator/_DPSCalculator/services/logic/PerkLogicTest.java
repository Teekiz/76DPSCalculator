package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.LoadoutService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkLogic;
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
	PerkLoaderService perkLoaderService;
	@Autowired
	ModLoaderService modLoaderService;
	@Autowired
	PerkLogic perkLogic;
	@Autowired
	LoadoutService loadoutService;

	@Test
	public void testFalseCondition() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();

		String weaponName = "10MMPISTOL";
		loadout.setWeapon(weaponName);
		assertNotNull(loadout.getWeapon());
		assertEquals("Test 10mm pistol", loadout.getWeapon().getWeaponName());

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		//
		boolean check = perkLogic.evaluateCondition(perk, loadout);
		assertFalse(check);
	}

	@Test
	public void testPerkWithDifferentReceivers() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();

		String weaponName = "10MMPISTOL";
		loadout.setWeapon(weaponName);
		assertNotNull(loadout.getWeapon());

		String perkName = "GUNSLINGER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		boolean check = perkLogic.evaluateCondition(perk, loadout);
		assertFalse(check);

		Receiver newReceiver = modLoaderService.getReceiver("CALIBRATE");
		if (loadout.getWeapon() instanceof RangedWeapon)
		{
			((RangedWeapon) loadout.getWeapon()).setReceiver(newReceiver);
		}

		boolean checkWithNewReceiver = perkLogic.evaluateCondition(perk, loadout);
		assertTrue(checkWithNewReceiver);

	}

	@Test
	public void testDamageEffect() throws IOException
	{
		String perkName = "GUNSLINGER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		perk.setPerkRank(1);
		Double perkValueR1 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.10, perkValueR1);

		perk.setPerkRank(2);
		Double perkValueR2 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.15, perkValueR2);

		perk.setPerkRank(3);
		Double perkValueR3 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.2, perkValueR3);

		perk.setPerkRank(0);
		Double perkValueLT1 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.10, perkValueLT1);

		perk.setPerkRank(-20);
		Double perkValueLT2 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.10, perkValueLT2);

		perk.setPerkRank(400);
		Double perkValueHT2 = (Double) perkLogic.applyEffect(perk);
		assertEquals(0.20, perkValueHT2);
	}
}

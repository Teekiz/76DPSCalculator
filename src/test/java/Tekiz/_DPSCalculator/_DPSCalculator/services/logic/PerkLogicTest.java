package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.Special;
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
	LoadoutService loadoutService;
	@Autowired
	PerkLoaderService perkLoaderService;
	@Autowired
	PerkLogic perkLogic;

	@Autowired
	ModLoaderService modLoaderService;

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
	public void testEffect() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		perkLogic.applyEffect(perk, loadout);
		assertEquals(2, loadout.getPlayer().getEndurance());
		loadout.getPlayer().modifySpecial(Special.ENDURANCE, -1);

		perk.setPerkRank(2);
		perkLogic.applyEffect(perk, loadout);
		assertEquals(3, loadout.getPlayer().getEndurance());
		loadout.getPlayer().modifySpecial(Special.ENDURANCE, -2);

		perk.setPerkRank(3);
		perkLogic.applyEffect(perk, loadout);
		assertEquals(4, loadout.getPlayer().getEndurance());
		loadout.getPlayer().modifySpecial(Special.ENDURANCE, -3);

	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
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
	LoadoutManager loadoutManager;
	@Autowired
	PerkLoaderService perkLoaderService;
	@Autowired
	PerkLogic perkLogic;

	@Autowired
	ModLoaderService modLoaderService;

	@Test
	public void testFalseCondition() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();

		String weaponName = "10MMPISTOL";
		loadout.getWeaponManager().setWeapon(weaponName);
		assertNotNull(loadout.getWeaponManager().getCurrentWeapon());
		assertEquals("Test 10mm pistol", loadout.getWeaponManager().getCurrentWeapon().getWeaponName());

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		boolean check = perkLogic.evaluateCondition(perk);
		assertFalse(check);
	}

	@Test
	public void testPerkWithDifferentReceivers() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();

		String weaponName = "10MMPISTOL";
		loadout.getWeaponManager().setWeapon(weaponName);
		assertNotNull(loadout.getWeaponManager().getCurrentWeapon());

		String perkName = "GUNSLINGER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		boolean check = perkLogic.evaluateCondition(perk);
		assertFalse(check);

		Receiver newReceiver = modLoaderService.getReceiver("CALIBRATE");
		if (loadout.getWeaponManager().getCurrentWeapon() instanceof RangedWeapon)
		{
			((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).setMod(newReceiver);
		}

		boolean checkWithNewReceiver = perkLogic.evaluateCondition(perk);
		assertTrue(checkWithNewReceiver);

	}

	@Test
	public void testEffect() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		perkLogic.applyEffect(perk);
		assertEquals(2, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -1);

		perk.setPerkRank(2);
		perkLogic.applyEffect(perk);
		assertEquals(3, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -2);

		perk.setPerkRank(3);
		perkLogic.applyEffect(perk);
		assertEquals(4, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -3);

	}
}

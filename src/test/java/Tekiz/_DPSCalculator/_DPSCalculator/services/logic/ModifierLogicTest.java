package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
	"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json",
	"consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json",
	"perk.data.file.path=src/test/resources/data/perkData/testPerks.json"})
public class ModifierLogicTest
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	ConsumableLoaderService consumableLoaderService;
	@Autowired
	PerkLoaderService perkLoaderService;
	@Autowired
	ModLoaderService modLoaderService;
	@Autowired
	ModifierLogic modifierLogic;

	@Test
	public void testPerkFalseCondition() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();

		String weaponName = "10MMPISTOL";
		loadout.getWeaponManager().setWeapon(weaponName);
		assertNotNull(loadout.getWeaponManager().getCurrentWeapon());
		assertEquals("Test 10mm pistol", loadout.getWeaponManager().getCurrentWeapon().getWeaponName());

		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		boolean check = modifierLogic.evaluateCondition(perk);
		assertFalse(check);
		loadoutManager.deleteAllLoadouts();
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

		boolean check = modifierLogic.evaluateCondition(perk);
		assertFalse(check);

		Receiver newReceiver = modLoaderService.getReceiver("CALIBRATE");
		if (loadout.getWeaponManager().getCurrentWeapon() instanceof RangedWeapon)
		{
			((RangedWeapon) loadout.getWeaponManager().getCurrentWeapon()).setMod(newReceiver);
		}

		boolean checkWithNewReceiver = modifierLogic.evaluateCondition(perk);
		assertTrue(checkWithNewReceiver);
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testPerkEffect() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");
		loadout.getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);

		String perkName = "GUNSLINGER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);

		modifierLogic.applyEffects(perk);
		assertEquals(2, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -1);

		perk.setRank(2);
		modifierLogic.applyEffects(perk);
		assertEquals(3, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -2);

		perk.setRank(3);
		modifierLogic.applyEffects(perk);
		assertEquals(4, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadout.getPlayerManager().getPlayer().getSpecials().modifySpecial(Specials.ENDURANCE, -3);

		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void TestConsumableToAddSpecial() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		Consumable consumable = consumableLoaderService.getConsumable("AGEDMIRELURKQUEENSTEAK");

		assertEquals(1, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		modifierLogic.applyEffects(consumable);
		assertEquals(4, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void TestConsumableAddToSpecialWithMultipleEffects() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");

		Consumable consumable = consumableLoaderService.getConsumable("TESTMULTIPLEEFFECTS");

		assertEquals(1, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		assertEquals(1, loadout.getPlayerManager().getPlayer().getSpecials().getIntelligence());
		modifierLogic.applyEffects(consumable);
		assertEquals(4, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		assertEquals(3, loadout.getPlayerManager().getPlayer().getSpecials().getIntelligence());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void TestConditionWithMetCriteria() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");
		Consumable consumable = consumableLoaderService.getConsumable("TESTCONDITION");
		assertTrue(modifierLogic.evaluateCondition(consumable));
		modifierLogic.applyEffects(consumable);
		assertEquals(6, loadout.getPlayerManager().getPlayer().getSpecials().getCharisma());
		loadoutManager.deleteAllLoadouts();
	}
}


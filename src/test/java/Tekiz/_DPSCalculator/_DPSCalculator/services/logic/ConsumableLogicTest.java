package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable.ConsumableLogic;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
	"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json", "consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json"})
public class ConsumableLogicTest
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	ConsumableLoaderService consumableLoaderService;
	@Autowired
	ConsumableLogic consumableLogic;

	@Test
	@DirtiesContext
	public void TestConsumableToAddSpecial() throws IOException
	{
		loadoutManager.deleteAllLoadouts();
		Loadout loadout = loadoutManager.getLoadout();
		Consumable consumable = consumableLoaderService.getConsumable("AGEDMIRELURKQUEENSTEAK");

		assertEquals(1, loadout.getPlayerManager().getPlayer().getSpecials().getEndurance());
		consumableLogic.applyEffect(consumable);
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
		consumableLogic.applyEffect(consumable);
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
		assertTrue(consumableLogic.evaluateCondition(consumable));
		consumableLogic.applyConditionEffect(consumable);
		assertEquals(6, loadout.getPlayerManager().getPlayer().getSpecials().getCharisma());
		loadoutManager.deleteAllLoadouts();
	}
}


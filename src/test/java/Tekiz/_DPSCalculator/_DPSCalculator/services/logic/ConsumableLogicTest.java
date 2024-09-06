package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.LoadoutService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.WeaponLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable.ConsumableLogic;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
	"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json", "consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json"})
public class ConsumableLogicTest
{
	@Autowired
	private LoadoutService loadoutService;
	@Autowired
	private ConsumableLoaderService consumableLoaderService;
	@Autowired
	private ConsumableLogic consumableLogic;

	@Test
	public void TestConsumableToAddSpecial() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();
		loadout.setWeapon("10MMPISTOL");

		Consumable consumable = consumableLoaderService.getConsumable("AGEDMIRELURKQUEENSTEAK");

		assertEquals(1, loadout.getPlayer().getSpecials().getEndurance());
		consumableLogic.applyEffect(consumable, loadout);
		assertEquals(4, loadout.getPlayer().getSpecials().getEndurance());
	}

	@Test
	public void TestConsumableAddToSpecialWithMultipleEffects() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();
		loadout.setWeapon("10MMPISTOL");

		Consumable consumable = consumableLoaderService.getConsumable("TESTMULTIPLEEFFECTS");

		assertEquals(1, loadout.getPlayer().getSpecials().getEndurance());
		assertEquals(1, loadout.getPlayer().getSpecials().getIntelligence());
		consumableLogic.applyEffect(consumable, loadout);
		assertEquals(4, loadout.getPlayer().getSpecials().getEndurance());
		assertEquals(3, loadout.getPlayer().getSpecials().getIntelligence());
	}

	@Test
	public void TestConditionWithMetCriteria() throws IOException
	{
		Loadout loadout = loadoutService.createNewLoadout();
		loadout.setWeapon("10MMPISTOL");
		Consumable consumable = consumableLoaderService.getConsumable("TESTCONDITION");
		assertTrue(consumableLogic.evaluateCondition(consumable, loadout));
		consumableLogic.applyConditionEffect(consumable, loadout);
		assertEquals(6, loadout.getPlayer().getSpecials().getCharisma());
	}
}


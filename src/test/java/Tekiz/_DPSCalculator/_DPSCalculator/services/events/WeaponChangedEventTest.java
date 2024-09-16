package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ModifierManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json", "perk.data.file.path=src/test/resources/data/perkData/testPerks.json", "receivers.data.file.path=src/test/resources/data/weaponData/testReceivers.json", "consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json"})
public class WeaponChangedEventTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	ParsingService parsingService;


	@Test
	public void testPerkWCE() throws IOException
	{
		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");
		assertEquals(1.00, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertNotNull(loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon());
		assertEquals(loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getWeaponType(), WeaponType.PISTOL);
		assertEquals(1.00, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertTrue(parsingService.evaluateCondition(null, loadoutManager.getLoadout().getPerkManager().getPerks().iterator().next().getCondition()));
		assertEquals(1.20, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testRemovePerkWCE() throws IOException
	{
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertEquals(1.00, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");
		assertEquals(1.20, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("AUTOMATIC", ModType.RECEIVER);
		assertEquals(1.00, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testConsumableWCE() throws IOException
	{
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENT");
		assertEquals(0, loadoutManager.getLoadout().getModifierManager().getSpecialModifier().getCharisma());

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertEquals(10, loadoutManager.getLoadout().getModifierManager().getSpecialModifier().getCharisma());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testRemoveConsumableWCE() throws IOException
	{
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENT");
		assertEquals(10, loadoutManager.getLoadout().getModifierManager().getSpecialModifier().getCharisma());

		loadoutManager.getLoadout().getWeaponManager().setWeapon("GAUSSRIFLE");
		assertEquals(0, loadoutManager.getLoadout().getModifierManager().getSpecialModifier().getCharisma());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testAddAndRemovePerkAndConsumableWCE() throws IOException
	{
		assertEquals(1.0, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENTTWO");
		loadoutManager.getLoadout().getPerkManager().addPerk("TESTMODIFIER");
		assertEquals(1.0, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertEquals(1.4, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());

		loadoutManager.getLoadout().getWeaponManager().setWeapon("GAUSSRIFLE");
		assertEquals(1.0, loadoutManager.getLoadout().getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus());
		loadoutManager.deleteAllLoadouts();
	}

}

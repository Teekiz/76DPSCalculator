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

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json", "perk.data.file.path=src/test/resources/data/perkData/testPerks.json", "receivers.data.file.path=src/test/resources/data/weaponData/testReceivers.json"})
public class WeaponChangedEventTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	ParsingService parsingService;

	@Test
	public void testExpression() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();

		loadout.getPerkManager().addPerk("HEAVYGUNNER");
		assertFalse(loadout.getPerkManager().getPerks().isEmpty());

		assertNotNull(loadout.getPerkManager().getPerks().iterator().next().getCondition());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testWeaponChangedEvent() throws IOException
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
	public void testWeaponChangedEventConditionRemoved() throws IOException
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
}

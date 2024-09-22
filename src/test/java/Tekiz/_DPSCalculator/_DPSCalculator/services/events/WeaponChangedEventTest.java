package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.config.TestInitializer;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(initializers = TestInitializer.class)
public class WeaponChangedEventTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	ParsingService parsingService;

	@Autowired
	DamageCalculationService damageCalculationService;

	@Autowired
	SpecialBonusCalculationService specialBonusCalculationService;


	@Test
	public void testPerkWCE() throws IOException
	{
		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");

		//condition to use perk does not match
		assertEquals(0.00, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertNotNull(loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon());
		assertEquals(loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getWeaponType(), WeaponType.PISTOL);

		//receiver reduces the damage by 0.1
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		//receiver does not change the damage
		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));
		loadoutManager.deleteAllLoadouts();
	}


	@Test
	public void testRemovePerkWCE() throws IOException
	{
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertEquals(28.0, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("AUTOMATIC", ModType.RECEIVER);
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.deleteAllLoadouts();
	}

	//todo - add in service to read specials


	@Test
	public void testConsumableWCE() throws IOException
	{
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENT");

		assertEquals(0, specialBonusCalculationService.getSpecialBonus(loadoutManager.getLoadout(), Specials.CHARISMA));

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(loadoutManager.getLoadout(), Specials.CHARISMA));
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testRemoveConsumableWCE() throws IOException
	{
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENT");
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(loadoutManager.getLoadout(), Specials.CHARISMA));

		loadoutManager.getLoadout().getWeaponManager().setWeapon("GAUSSRIFLE");
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(loadoutManager.getLoadout(), Specials.CHARISMA));
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testAddAndRemovePerkAndConsumableWCE() throws IOException
	{
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENTTWO");
		loadoutManager.getLoadout().getPerkManager().addPerk("TESTMODIFIER");
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		assertEquals(36.4, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getWeaponManager().setWeapon("GAUSSRIFLE");
		assertEquals(140.0, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));
		loadoutManager.deleteAllLoadouts();
	}
}

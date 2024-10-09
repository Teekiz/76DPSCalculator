package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class WeaponChangedEventTest
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	DamageCalculationService damageCalculationService;
	@Autowired
	SpecialBonusCalculationService specialBonusCalculationService;


	@Test
	public void testPerkWCE() throws IOException
	{
		log.debug("{}Running test - testPerkWCE in WeaponChangedEventTest.", System.lineSeparator());
		perkManager.addPerk("TESTEVENT");

		//condition to use perk does not match
		assertEquals(0.00, damageCalculationService.calculateOutgoingDamage());

		weaponManager.setWeapon("10MMPISTOL");
		assertNotNull(weaponManager.getWeapon());
		assertEquals(weaponManager.getWeapon().getWeaponType(), WeaponType.PISTOL);

		//receiver reduces the damage by 0.1
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage());

		//receiver does not change the damage
		weaponManager.modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage());
		loadoutManager.deleteAllLoadouts();
	}


	@Test
	public void testRemovePerkWCE() throws IOException
	{
		log.debug("{}Running test - testRemovePerkWCE in WeaponChangedEventTest.", System.lineSeparator());
		weaponManager.setWeapon("10MMPISTOL");
		weaponManager.modifyWeapon("CALIBRATE", ModType.RECEIVER);
		assertEquals(28.0, damageCalculationService.calculateOutgoingDamage());

		perkManager.addPerk("TESTEVENT");
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage());

		weaponManager.modifyWeapon("AUTOMATIC", ModType.RECEIVER);
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage());
		loadoutManager.deleteAllLoadouts();
	}

	//todo - add in service to read specials


	@Test
	public void testConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		consumableManager.addConsumable("TESTEVENT");

		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));

		weaponManager.setWeapon("10MMPISTOL");
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testRemoveConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testRemoveConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		weaponManager.setWeapon("10MMPISTOL");
		consumableManager.addConsumable("TESTEVENT");
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));

		weaponManager.setWeapon("GAUSSRIFLE");
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testAddAndRemovePerkAndConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testAddAndRemovePerkAndConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage());
		consumableManager.addConsumable("TESTEVENTTWO");
		perkManager.addPerk("TESTMODIFIER");
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage());

		weaponManager.setWeapon("10MMPISTOL");
		assertEquals(36.4, damageCalculationService.calculateOutgoingDamage());

		weaponManager.setWeapon("GAUSSRIFLE");
		assertEquals(140.0, damageCalculationService.calculateOutgoingDamage());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testConsumableWithAdditionalContext() throws IOException
	{
		log.debug("{}Running test - testConsumableWithAdditionalContext in WeaponChangedEventTest.", System.lineSeparator());
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE));
		consumableManager.addConsumable("TESTCONDITION");
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));
		assertEquals(2, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE));
		//this should enable the condition to boost charisma
		weaponManager.setWeapon("10MMPISTOL");
		assertEquals(5, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA));
		loadoutManager.deleteAllLoadouts();
	}
}

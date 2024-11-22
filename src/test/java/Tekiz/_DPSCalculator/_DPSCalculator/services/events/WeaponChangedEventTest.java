package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
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
	UserLoadoutTracker userLoadoutTracker;
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
		Loadout loadout = loadoutManager.getLoadout(1);
		perkManager.addPerk("perks5", loadout); //TESTEVENT

		//condition to use perk does not match
		assertEquals(0.00, damageCalculationService.calculateOutgoingDamage(loadout));

		weaponManager.setWeapon("weapo2", loadout); //10MMPISTOL
		assertNotNull(loadout.getWeapon());
		assertEquals(loadout.getWeapon().getWeaponType(), WeaponType.PISTOL);

		//receiver reduces the damage by 0.1
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadout));

		//receiver does not change the damage
		weaponManager.modifyWeapon("modRe2", ModType.RECEIVER, loadout);//CALIBRATE
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}


	@Test
	public void testRemovePerkWCE() throws IOException
	{
		log.debug("{}Running test - testRemovePerkWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("modRe2", ModType.RECEIVER, loadout);//CALLIBRATE
		assertEquals(28.0, damageCalculationService.calculateOutgoingDamage(loadout));

		perkManager.addPerk("perks5", loadout); //TESTEVENT
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadout));

		weaponManager.modifyWeapon("modRe1", ModType.RECEIVER, loadout);//AUTOMATIC
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	//todo - add in service to read specials


	@Test
	public void testConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		consumableManager.addConsumable("consu7", loadout);//TESTEVENT

		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));

		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testRemoveConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testRemoveConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		consumableManager.addConsumable("consu7", loadout); //TEST EVENT
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));

		weaponManager.setWeapon("weapo3", loadout);//GAUSSRIFLE
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testAddAndRemovePerkAndConsumableWCE() throws IOException
	{
		log.debug("{}Running test - testAddAndRemovePerkAndConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadout));
		consumableManager.addConsumable("consu16", loadout); //TESTEVENTTWO
		perkManager.addPerk("perks6", loadout);//TESTMODIFIER
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadout));

		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		assertEquals(36.4, damageCalculationService.calculateOutgoingDamage(loadout));

		weaponManager.setWeapon("weapo3", loadout);//GAUSSRIFLE
		assertEquals(140.0, damageCalculationService.calculateOutgoingDamage(loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testConsumableWithAdditionalContext() throws IOException
	{
		log.debug("{}Running test - testConsumableWithAdditionalContext in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE, loadout));
		consumableManager.addConsumable("consu6", loadout);//TESTCONDITION
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		assertEquals(2, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE, loadout));
		//this should enable the condition to boost charisma
		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		assertEquals(5, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

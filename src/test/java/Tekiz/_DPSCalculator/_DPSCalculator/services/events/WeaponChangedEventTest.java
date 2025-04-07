package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class WeaponChangedEventTest extends BaseTestClass
{
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

	//TEST OBJECT IDS
	String _10MMPISTOL;
	String CALIBRATE;
	String TESTEVENT;
	String TESTEVENTTWO;
	String TESTEVENTCONSUMABLE;
	String TESTCONDITION;
	String AUTOMATIC;
	String TESTMODIFIER;
	String ASSAULTRONBLADE;
	String GAUSSRIFLE;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		ASSAULTRONBLADE = jsonIDMapper.getIDFromFileName("ASSAULTRONBLADE");
		GAUSSRIFLE = jsonIDMapper.getIDFromFileName("GAUSSRIFLE");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
		TESTEVENT = jsonIDMapper.getIDFromFileName("TESTEVENT");
		TESTEVENTCONSUMABLE = jsonIDMapper.getIDFromFileName("TESTEVENTCONSUMABLE");
		TESTEVENTTWO = jsonIDMapper.getIDFromFileName("TESTEVENTTWO");
		TESTCONDITION = jsonIDMapper.getIDFromFileName("TESTCONDITION");
		AUTOMATIC = jsonIDMapper.getIDFromFileName("AUTOMATIC");
		TESTMODIFIER = jsonIDMapper.getIDFromFileName("TESTMODIFIER");
	}

	@Test
	public void testPerkWCE() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testPerkWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		perkManager.addPerk(TESTEVENT, loadout); //TESTEVENT

		//condition to use perk does not match
		assertEquals(0.00, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		weaponManager.setWeapon(_10MMPISTOL, loadout); //10MMPISTOL
		assertNotNull(loadout.getWeapon());
		assertEquals(loadout.getWeapon().getWeaponType(), WeaponType.PISTOL);

		//receiver reduces the damage by 0.1
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		//receiver does not change the damage
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}


	@Test
	public void testRemovePerkWCE() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testRemovePerkWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE
		assertEquals(28.0, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		perkManager.addPerk(TESTEVENT, loadout); //TESTEVENT
		assertEquals(33.6, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		weaponManager.modifyWeapon(AUTOMATIC, loadout);//AUTOMATIC
		assertEquals(25.2, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	//todo - add in service to read specials


	@Test
	public void testConsumableWCE() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		consumableManager.addConsumable(TESTEVENTCONSUMABLE, loadout);//TESTEVENTCONSUMABLE

		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));

		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);//10MMPISTOL
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testRemoveConsumableWCE() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testRemoveConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);//10MMPISTOL
		consumableManager.addConsumable(TESTEVENTCONSUMABLE, loadout); //TESTEVENTCONSUMABLE
		assertEquals(10, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));

		weaponManager.setWeapon(GAUSSRIFLE, loadout);//GAUSSRIFLE
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testAddAndRemovePerkAndConsumableWCE() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testAddAndRemovePerkAndConsumableWCE in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		consumableManager.addConsumable(TESTEVENTTWO, loadout); //TESTEVENTTWO
		perkManager.addPerk(TESTMODIFIER, loadout);//TESTMODIFIER
		assertEquals(0.0, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		assertEquals(36.4, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		weaponManager.setWeapon(GAUSSRIFLE, loadout);//GAUSSRIFLE
		assertEquals(140.0, damageCalculationService.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testConsumableWithAdditionalContext() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testConsumableWithAdditionalContext in WeaponChangedEventTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE, loadout));
		consumableManager.addConsumable(TESTCONDITION, loadout);//TESTCONDITION
		assertEquals(0, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		assertEquals(2, specialBonusCalculationService.getSpecialBonus(Specials.INTELLIGENCE, loadout));
		//this should enable the condition to boost charisma
		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);//10MMPISTOL
		assertEquals(5, specialBonusCalculationService.getSpecialBonus(Specials.CHARISMA, loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

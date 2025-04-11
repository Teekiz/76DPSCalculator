package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.AttackType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.CriticalDamageBonusCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CriticalDamageCalculationTest extends BaseTestClass
{
	//need to test how - dot, sneak damage, multiple damage types, modifiers
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	CriticalDamageBonusCalculator criticalDamageBonusCalculator;

	Loadout loadout;

	String _10MMPISTOL;
	String CALIBRATE;
	String ASSAULTRONBLADE;
	String BETTERCRITICALS;
	String IGUANASOUP;
	String CRITICALSAVVY;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
		ASSAULTRONBLADE = jsonIDMapper.getIDFromFileName("ASSAULTRONBLADE");
		BETTERCRITICALS = jsonIDMapper.getIDFromFileName("BETTERCRITICALS");
		CRITICALSAVVY = jsonIDMapper.getIDFromFileName("CRITICALSAVVY");
		IGUANASOUP = jsonIDMapper.getIDFromFileName("IGUANASOUP");

		loadout = loadoutManager.getLoadout(1);
		loadout.getPlayer().setAttackType(AttackType.VATS);
	}

	@Test
	public void testCriticalDamage_WithRangedWeapon_NoModifiers() throws IOException, ResourceNotFoundException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		loadout.getPlayer().getSpecials().setSpecial(Specials.LUCK, 10);
		WeaponDamage weaponDamage = loadout.getWeapon().getWeaponDamageByLevel().get(45).getFirst();

		double baseDamage = 28.0;
		double bonusDamage = 0;

		//critical consumption should be 100%
		//recharge rate = (10 - 1) * 1.5 + 6.5 = 20
		//shots required = (100 / 20) = 5

		//base critical damage = 100 % + 0 (receiver) = 100%
		//critical damage = 28 * 1.0 = 28

		//critical damage per shot = 28 / 5 = 5.6 bonus damage each shot
		double averageDamagePerShot = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, bonusDamage, 0, weaponDamage, dpsDetails);
		assertEquals(5.6, averageDamagePerShot);
		assertEquals(5, dpsDetails.getShotsRequiredToFillCriticalMeter());
	}

	@Test
	public void testCriticalDamage_WitMeleeWeapon_NoModifiers() throws IOException, ResourceNotFoundException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);
		loadout.getPlayer().getSpecials().setSpecial(Specials.LUCK, 10);
		WeaponDamage weaponDamage = loadout.getWeapon().getWeaponDamageByLevel().get(45).getFirst();

		double baseDamage = 28.0;
		double bonusDamage = 0;

		//critical consumption should be 100%
		//recharge rate = (10 - 1) * 1.5 + 6.5 = 20
		//shots required = (100 / 20) = 5

		//base critical damage = 100 % + 0 (receiver) = 100%
		//critical damage = (28 * 1.0) * 1.5 = 42

		//critical damage per shot = 42 / 5 = 8.4 bonus damage each shot
		double averageDamagePerShot = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, bonusDamage, 0, weaponDamage, dpsDetails);
		assertEquals(8.4, averageDamagePerShot);
		assertEquals(5, dpsDetails.getShotsRequiredToFillCriticalMeter());
	}

	@Test
	public void testCriticalDamage_WitNullWeapon_NoModifiers() throws IOException, ResourceNotFoundException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		loadout.getPlayer().getSpecials().setSpecial(Specials.LUCK, 10);

		double baseDamage = 28.0;
		double bonusDamage = 0;

		double averageDamagePerShot = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, bonusDamage, 0, null, dpsDetails);
		assertEquals(0, averageDamagePerShot);
	}

	@Test
	public void testCriticalDamage_WithRangedWeapon_WithModifiers() throws IOException, ResourceNotFoundException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		loadout.getPlayer().getSpecials().setSpecial(Specials.LUCK, 10);
		WeaponDamage weaponDamage = loadout.getWeapon().getWeaponDamageByLevel().get(45).getFirst();

		perkManager.addPerk(BETTERCRITICALS, loadout);
		perkManager.addPerk(CRITICALSAVVY, loadout);
		perkManager.changePerkRank(CRITICALSAVVY, 3, loadout);

		double baseDamage = 28.0;
		double bonusDamage = 0;

		//critical consumption should be 55%
		//recharge rate = (10 - 1) * 1.5 + 6.5 = 20
		//shots required = (55 / 20) = 3

		//base critical damage = 100 % + 0 (receiver) = 100%
		//critical damage = 28 * 1.5 = 42

		//critical damage per shot = 42 / 3 = 14 bonus damage each shot
		double averageDamagePerShot = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, bonusDamage, 0, weaponDamage, dpsDetails);
		assertEquals(14, averageDamagePerShot);
		assertEquals(3, dpsDetails.getShotsRequiredToFillCriticalMeter());
	}

	@Test
	public void testCriticalDamage_WithRangedWeapon_WithModifiedLuck() throws IOException, ResourceNotFoundException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		loadout.getPlayer().getSpecials().setSpecial(Specials.LUCK, 15);
		consumableManager.addConsumable(IGUANASOUP, loadout);
		WeaponDamage weaponDamage = loadout.getWeapon().getWeaponDamageByLevel().get(45).getFirst();

		double baseDamage = 28.0;
		double bonusDamage = 0;

		//critical consumption should be 100%
		//recharge rate = (17 - 1) * 1.5 + 6.5 = 30.5
		//shots required = (100 / 30.5) = 4

		//base critical damage = 100 % + 0 (receiver) = 100%
		//critical damage = 28 * 1.0 = 28

		//critical damage per shot = 28 / 4 = 7 bonus damage each shot
		double averageDamagePerShot = criticalDamageBonusCalculator.addAverageCriticalDamagePerShot(loadout, baseDamage, bonusDamage, 0, weaponDamage, dpsDetails);
		assertEquals(7, averageDamagePerShot);
		assertEquals(4, dpsDetails.getShotsRequiredToFillCriticalMeter());
	}
}

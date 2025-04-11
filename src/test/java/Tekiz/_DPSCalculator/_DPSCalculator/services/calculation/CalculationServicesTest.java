package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.Limbs;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.AttackType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnemyManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class CalculationServicesTest extends BaseTestClass
{
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	EnemyManager enemyManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	PlayerManager playerManager;
	@Autowired
	DamageCalculationService calculator;

	//TEST OBJECT IDS
	String _10MMPISTOL;
	String CALIBRATE;
	String TESTEVENT;
	String TESTEVENTTWO;
	String TENDERIZER;
	String MR_GUTSY;
	String BETTERCRITICALS;
	String IGUANASOUP;
	String CRITICALSAVVY;
	String COVERT_OPERATIVE;
	String SQUIRRELONASTICK;
	String COOKEDSOFTSHELLMEAT;
	String TESTMODIFIER;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
		TESTEVENT = jsonIDMapper.getIDFromFileName("TESTEVENT");
		TESTEVENTTWO = jsonIDMapper.getIDFromFileName("TESTEVENTTWO");
		TENDERIZER = jsonIDMapper.getIDFromFileName("TENDERIZER");
		MR_GUTSY = jsonIDMapper.getIDFromFileName("MR_GUTSY");
		BETTERCRITICALS = jsonIDMapper.getIDFromFileName("BETTERCRITICALS");
		CRITICALSAVVY = jsonIDMapper.getIDFromFileName("CRITICALSAVVY");
		IGUANASOUP = jsonIDMapper.getIDFromFileName("IGUANASOUP");
		COVERT_OPERATIVE = jsonIDMapper.getIDFromFileName("COVERT_OPERATIVE");
		SQUIRRELONASTICK = jsonIDMapper.getIDFromFileName("SQUIRRELONASTICK");
		COOKEDSOFTSHELLMEAT = jsonIDMapper.getIDFromFileName("COOKEDSOFTSHELLMEAT");
		TESTMODIFIER = jsonIDMapper.getIDFromFileName("TESTMODIFIER");
	}

	@Test
	public void testWithBaseDamage() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testWithBaseDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE

		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		//removing the perk should reduce the damage by 20%
		perkManager.removePerk(TESTEVENT, loadout);//TESTEVENT
		assertEquals(33.6, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testMultiplicativeDamage() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testMultiplicativeDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE
		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		//level 1 tenderizer should add 5% extra damage on top of the existing damage
		//39.2 * (1 + 0.05) = 41.16 (41.2)
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER
		assertEquals(41.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testDamageResistance in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		// based on this from the wiki: https://fallout.fandom.com/wiki/Damage_(Fallout_76)
		//A semi-automatic rifle that deals 100 Physical WeaponDamage against a target with 200 (Physical) WeaponDamage Resist will deal 38 Physical WeaponDamage:

		HashMap<Integer, List<WeaponDamage>> damageHashMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList = new ArrayList<>();
		weaponDamageList.add(new WeaponDamage(DamageType.PHYSICAL, 100.0, 0));
		damageHashMap.put(45, weaponDamageList);

		RangedWeapon weapon = RangedWeapon.builder().weaponDamageByLevel(damageHashMap).build();

		Enemy enemy = new Enemy("1", "NA", false, EnemyType.HUMAN,
			new ArmourResistance(200, 0,0,0,0,0), Limbs.TORSO);

		loadout.setWeapon(weapon);
		loadout.setEnemy(enemy);

		//38.85 rounded up -> 38.9
		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);
		assertEquals(38.9, dpsDetails.getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_WithPenetrationLegendaryEffect() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testDamageResistance_WithPenetrationLegendaryEffect in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		// based on this from the wiki: https://fallout.fandom.com/wiki/Damage_(Fallout_76)
		//A semi-automatic rifle that deals 100 Physical WeaponDamage against a target with 200 (Physical) WeaponDamage Resist will deal 38 Physical WeaponDamage:

		HashMap<ModifierTypes, ModifierValue<?>> map = new HashMap<>();
		map.put(ModifierTypes.PENETRATION, new ModifierValue<>(ModifierTypes.PENETRATION, 0.36));

		LegendaryEffect legendaryEffect = new LegendaryEffect("1", "testEffect", "", ModifierSource.LEGENDARY_EFFECT,
			new ArrayList<>(), StarType._1STAR, null, map);

		HashMap<Integer, List<WeaponDamage>> damageHashMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList = new ArrayList<>();
		weaponDamageList.add(new WeaponDamage(DamageType.PHYSICAL, 100.0, 0));
		damageHashMap.put(45, weaponDamageList);

		RangedWeapon weapon = RangedWeapon.builder().weaponDamageByLevel(damageHashMap).legendaryEffects(new LegendaryEffectsMap()).build();
		weapon.getLegendaryEffects().addLegendaryEffect(legendaryEffect);

		Enemy enemy = new Enemy("1", "NA", false, EnemyType.HUMAN,
			new ArmourResistance(200, 0,0,0,0,0), Limbs.TORSO);

		loadout.setWeapon(weapon);
		loadout.setEnemy(enemy);

		//38.85 rounded up -> 38.9 with 36% armour penetration = 45.72 (45.7)
		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);
		assertEquals(45.7, dpsDetails.getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_WithPreviousValues() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testDamageResistance_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE

		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		enemyManager.changeEnemy(MR_GUTSY, loadout);
		loadout.getEnemy().setTargetedLimb(Limbs.THRUSTER);

		//39 with a physical resistance of 6 - 0.992 (lowest value is 0.99), therefore 39.2 * 0.99 = 38.8
		assertEquals(38.8, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_ImmuneToDamageType() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testDamageResistance_ImmuneToDamageType in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		// based on this from the wiki: https://fallout.fandom.com/wiki/Damage_(Fallout_76)
		//A semi-automatic rifle that deals 100 Physical WeaponDamage against a target with 200 (Physical) WeaponDamage Resist will deal 38 Physical WeaponDamage:

		HashMap<Integer, List<WeaponDamage>> damageHashMap = new HashMap<>();
		List<WeaponDamage> weaponDamageList = new ArrayList<>();
		weaponDamageList.add(new WeaponDamage(DamageType.RADIATION, 100.0, 0));
		damageHashMap.put(45, weaponDamageList);

		RangedWeapon weapon = RangedWeapon.builder().weaponDamageByLevel(damageHashMap).build();

		Enemy enemy = new Enemy("1", "NA", false, EnemyType.HUMAN,
			new ArmourResistance(200, 0,2000000,0,0,0), Limbs.TORSO);

		loadout.setWeapon(weapon);
		loadout.setEnemy(enemy);

		//immune to radiation damage, should return 0.
		assertEquals(0, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testBodyPartMultiplier_WithPreviousValues() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testBodyPartMultiplier_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE

		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		enemyManager.changeEnemy(MR_GUTSY, loadout);//MR_GUTSY
		loadout.getEnemy().setTargetedLimb(Limbs.MIDDLE_EYE);

		//39 with a physical resistance of 6 - 0.992 (lowest value is 0.99), therefore 39.2 * 0.99 = 38.8
		//eyes provide a 1.25 damage multiplier - 38.8 * 1.25 = 48.5
		assertEquals(48.5, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testVATSDamage_withDoTDamage_withSneakDamage() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testBodyPartMultiplier_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALIBRATE
		loadout.getWeapon().getWeaponDamageByLevel().get(45).add(new WeaponDamage(DamageType.BLEED, 60, 3.0));

		playerManager.setSpecial(loadout, Specials.AGILITY, 5);
		playerManager.setSpecial(loadout, Specials.LUCK, 10);
		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO
		perkManager.addPerk(TENDERIZER, loadout);
		perkManager.addPerk(BETTERCRITICALS, loadout);
		perkManager.addPerk(CRITICALSAVVY, loadout);
		perkManager.changePerkRank(CRITICALSAVVY, 3, loadout);
		perkManager.addPerk(COVERT_OPERATIVE, loadout);
		consumableManager.addConsumable(IGUANASOUP, loadout);
		consumableManager.addConsumable(SQUIRRELONASTICK, loadout);
		consumableManager.addConsumable(COOKEDSOFTSHELLMEAT, loadout);

		loadout.getPlayer().setSneaking(true);
		loadout.getPlayer().setAttackType(AttackType.VATS);

		//weapon damage = physical and bleed
		//BASE AND ADDITIVE DAMAGE:
		//PHYSICAL = 28.0 * (1 + 0.2 + 0.2) = 39.2
		//BLEED = 20 * (1 + 0.2 + 0.2) = 28

		//SNEAK BONUSES AND MULTIPLICATIVE
		//PHYSICAL = 28 * (1.4 + 1.15) * 1.05 = 74.97
		//BLEED = 20 * (1.4 + 1.15) * 1.05 = 53.55

		//CRITICAL BONUS
		//BASE = 1.0 - With bonuses = 1.5
		//DAMAGE CRITICAL = 28 * 2.5 = 70
		//CRITICAL CONSUMPTION = 55%
		//recharge rate = (13 - 1) * 1.5 + 6.5 = 24.5
		//shots required = (55 / 24.5) = 3

		//Damage - PHYSICAL
		//Critical damage = (39.2 + 70) = 109.2
		//Critical Damage Per Shot = 109.2 / 3 = 36.4
		//Non-Critical Damage = 74.97 - (74.97 / 3) = 49.98
		//Damage Total Per Shot = 49.98 + 36.4 = 86.38

		//Damage Total Per Shot = 86.38 + 53.55 = 139.93

		//AP BONUS
		//3 AGI from squirrel on a stick, 20 max ap from soft shell meat
		//AP PER SHOT = receiver modifiers base rate of 20 by 0 (20 AP per shot)
		//MAX AP = 60 + (10 * Agility) + BONUS = 60 + (10 * 8) + 20 = 160 - 5 = 155

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//AP Duration = 155 / (20 * 4.3) = 1.8

		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);

		assertEquals(86.4, round(dpsDetails.getDamagePerShot().get(DamageType.PHYSICAL)));
		assertEquals(1.8, round(dpsDetails.getTimeToConsumeActionPoints()));
		assertEquals(109.2, round(dpsDetails.getCriticalDamagePerShot().get(DamageType.PHYSICAL)));
	}

	@Test
	public void testVATSDamage_withMultipleReloads() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testBodyPartMultiplier_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL

		playerManager.setSpecial(loadout, Specials.AGILITY, 15);
		playerManager.setSpecial(loadout, Specials.LUCK, 10);
		perkManager.addPerk(TESTEVENT, loadout);//TESTEVENT
		consumableManager.addConsumable(TESTEVENTTWO, loadout);//TESTEVENTTWO
		perkManager.addPerk(TENDERIZER, loadout);
		perkManager.addPerk(BETTERCRITICALS, loadout);
		perkManager.addPerk(CRITICALSAVVY, loadout);
		perkManager.changePerkRank(CRITICALSAVVY, 3, loadout);
		consumableManager.addConsumable(IGUANASOUP, loadout);
		consumableManager.addConsumable(SQUIRRELONASTICK, loadout);
		perkManager.addPerk(TESTMODIFIER, loadout);
		consumableManager.addConsumable(COOKEDSOFTSHELLMEAT, loadout);

		loadout.getPlayer().setAttackType(AttackType.VATS);

		//weapon damage = physical and bleed
		//BASE AND ADDITIVE DAMAGE:
		//PHYSICAL = 28.0 * (1 + 0.2 + 0.2 - 0.1) = 36.4

		//SNEAK BONUSES AND MULTIPLICATIVE
		//PHYSICAL = 28 * (1.3) * 1.05 = 38.22

		//CRITICAL BONUS
		//BASE = 1.0 - With bonuses = 0.5
		//DAMAGE CRITICAL = 28 * 1.5 = 42
		//CRITICAL CONSUMPTION = 55%
		//recharge rate = (13 - 1) * 1.5 + 6.5 = 24.5
		//shots required = (55 / 24.5) = 3

		//Damage - PHYSICAL
		//Critical damage = (36.4 + 42) = 78.4
		//Critical Damage Per Shot = 78.4 / 3 = 26.13
		//Non-Critical Damage = 38.22 - (38.22 / 3) = 25.48
		//Damage Total Per Shot = 26.13 + 25.48 = 51.61

		//Damage Total Per Shot = 51.61 + 0 = 51.61

		//AP BONUS
		//3 AGI from squirrel on a stick, 20 max ap from soft shell meat
		//AP PER SHOT = receiver modifiers base rate of 20 by 10 (10 AP per shot)
		//MAX AP = 60 + (10 * Agility) + BONUS = 60 + (10 * 18) + 20 = 160 - 5 = 255

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//Shots per second = (32 + 43) / 10 = 7.5

		//AP Duration = 255 / (10 * 7.5) = 3.40
		//Time to empty magazine = 12 / 7.5 = 1.6

		//reload time = 1.97
		//extra magazines = 3.4 / 1.6 = 2.1 (2)
		//total cycle time = 3.4 + (1.97 * 2) = 7.34

		//Damage per magazine = 51.61 * 12 = 619.32
		//Damage per second = 619.32 / 7.34 = 84.38


		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);

		assertEquals(51.6, round(dpsDetails.getDamagePerShot().get(DamageType.PHYSICAL)));
		assertEquals(3.4, round(dpsDetails.getTimeToConsumeActionPoints()));
		assertEquals(78.4, round(dpsDetails.getCriticalDamagePerShot().get(DamageType.PHYSICAL)));
		assertEquals(84.4, round(dpsDetails.getTotalDamagePerSecond()));
	}
}

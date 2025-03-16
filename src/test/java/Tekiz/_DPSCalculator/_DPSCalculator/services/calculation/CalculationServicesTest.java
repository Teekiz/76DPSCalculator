package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.Limbs;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnemyManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
	DamageCalculationService calculator;

	@Test
	public void testWithBaseDamage() throws IOException
	{
		log.debug("{}Running test - testWithBaseDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon("WEAPONS1", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE

		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout, true));

		//removing the perk should reduce the damage by 20%
		perkManager.removePerk("PERKS5", loadout);//TESTEVENT
		assertEquals(33.6, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testMultiplicativeDamage() throws IOException
	{
		log.debug("{}Running test - testMultiplicativeDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		weaponManager.setWeapon("WEAPONS1", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE
		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout, true));

		//level 1 tenderizer should add 5% extra damage on top of the existing damage
		//39.2 * (1 + 0.05) = 41.16 (41.2)
		perkManager.addPerk("perks4", loadout);//TENDERIZER
		assertEquals(41.2, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance() throws IOException
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
		assertEquals(38.9, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_WithPenetrationLegendaryEffect() throws IOException
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
		assertEquals(45.7, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_WithPreviousValues() throws IOException
	{
		log.debug("{}Running test - testDamageResistance_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon("WEAPONS1", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE

		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout, true));

		enemyManager.changeEnemy("ENEMIES2", loadout);
		loadout.getEnemy().setTargetedLimb(Limbs.THRUSTER);

		//39 with a physical resistance of 6 - 0.992 (lowest value is 0.99), therefore 39.2 * 0.99 = 38.8
		assertEquals(38.8, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testDamageResistance_ImmuneToDamageType() throws IOException
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
		assertEquals(0, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testBodyPartMultiplier_WithPreviousValues() throws IOException
	{
		log.debug("{}Running test - testBodyPartMultiplier_WithPreviousValues in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon("WEAPONS1", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE

		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout, true));

		enemyManager.changeEnemy("ENEMIES2", loadout);
		loadout.getEnemy().setTargetedLimb(Limbs.MIDDLE_EYE);

		//39 with a physical resistance of 6 - 0.992 (lowest value is 0.99), therefore 39.2 * 0.99 = 38.8
		//eyes provide a 1.25 damage multiplier - 38.8 * 1.25 = 48.5
		assertEquals(48.5, calculator.calculateOutgoingDamage(loadout, true));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

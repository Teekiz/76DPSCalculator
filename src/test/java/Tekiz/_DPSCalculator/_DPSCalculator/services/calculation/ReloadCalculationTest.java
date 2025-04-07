package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PerSecond.RangedDamageCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ReloadCalculationTest extends BaseTestClass
{
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	DamageCalculationService calculator;
	@Autowired
	RangedDamageCalculator rangedDamageCalculator;

	String _10MMPISTOL;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
	}

	@Test
	public void reloadCalculationTest_WithStandardReload() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		weaponManager.setWeapon(_10MMPISTOL, loadout);

		/*
			without additional modifiers
			DAMAGE PER SHOT: 28.0
			MAGAZINE SIZE: 12
			FIRERATE: 43 + 32
			RELOADTIME: 1.97

			//SHOTS PER SECOND = (43 + 32) /10 = 7.5.
			//TIME TO USE UP MAGAZINE = 12 / 7.5 = 1.6
			//TOTAL CYCLE = 1.6 + 1.97 = 3.57
			//DAMAGE PER CYCLE = 28.0 * 12 = 336
			//DAMAGE PER SECOND WITHOUT RELOAD = 336 / 1.6 = 210
			//DAMAGE PER SECOND WITH RELOAD = 336 / 3.57 = 94.12
		 */

		DPSDetails dpsDetails = new DPSDetails(1);
		double dps = rangedDamageCalculator.calculateDPSWithReload(28, 0, loadout, dpsDetails);

		assertEquals(94.12, super.round(dps));
		assertEquals(7.5, dpsDetails.getShotsPerSecond());
		assertEquals(1.6, super.round(dpsDetails.getTimeToEmptyMagazine()));
	}

	@Test
	public void reloadCalculationTest_WithDoTDamage() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		WeaponDamage weaponDamage = new WeaponDamage(DamageType.PHYSICAL, 28.0, 0);
		WeaponDamage dotDamage = new WeaponDamage(DamageType.FIRE, 9.0, 3);
		List<WeaponDamage> weaponDamagesList = new ArrayList<>(Arrays.asList(weaponDamage, dotDamage));
		HashMap<Integer, List<WeaponDamage>> hashMap = new HashMap<>();
		hashMap.put(45, weaponDamagesList);

		RangedWeapon rangedWeapon = RangedWeapon.builder()
			.weaponDamageByLevel(hashMap)
			.magazineSize(12)
			.reloadTime(1.97)
			.fireRate(43)
			.build();

		loadout.setWeapon(rangedWeapon);

		/*
			without additional modifiers
			DAMAGE PER SHOT: 28.0
			MAGAZINE SIZE: 12
			FIRERATE: 43
			RELOADTIME: 1.97

			STANDARD
			SHOTS PER SECOND = 43/10 = 4.3.
			TIME TO USE UP MAGAZINE = 12 / 4.3 = 2.79
			TOTAL CYCLE = 2.79 + 1.97 = 4.76
			DAMAGE PER CYCLE = 28.0 * 12 = 336
			DAMAGE PER SECOND WITHOUT RELOAD = 336 / 2.79 = 120.4
			DAMAGE PER SECOND WITH RELOAD = 336 / 4.76 = 70.58

			DOT
			DAMAGE OVER TIME = 9 / 3 = 3
			DAMAGE WHILE RELOADING = 1.97 = 1 = 3.
			DAMAGE PER CYCLE = 3 * 12 = 36 + 3 = 39
			DAMAGE PER SECOND WITHOUT RELOAD = 39 / 2.79 = 13.97
			DAMAGE PER SECOND WITH RELOAD = 39 / 4.76 = 8.19

		 */

		DPSDetails dpsDetails = new DPSDetails(1);
		for (WeaponDamage damage : rangedWeapon.getBaseDamage(45)){

			double damageValue = damage.damage();
			if (damage.damageType().equals(DamageType.FIRE)){
				//in actual tests, this value would have been divided before reaching this service
				damageValue = 3;
			}
			double dps = rangedDamageCalculator.calculateDPSWithReload(damageValue, damage.overTime(), loadout, dpsDetails);
			dpsDetails.getDamagePerSecond().put(damage.damageType(), dps);
		}

		assertEquals(70.58, super.round(dpsDetails.getDamagePerSecond().get(DamageType.PHYSICAL)));
		assertEquals(8.19, super.round(dpsDetails.getDamagePerSecond().get(DamageType.FIRE)));

		assertEquals(4.3, dpsDetails.getShotsPerSecond());
		assertEquals(2.79, super.round(dpsDetails.getTimeToEmptyMagazine()));

		assertEquals(78.77, super.round(dpsDetails.getTotalDamagePerSecond()));
	}

	@Test
	public void reloadCalculationTest_WithReloadAsZero() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		WeaponDamage weaponDamage = new WeaponDamage(DamageType.PHYSICAL, 28.0, 0);
		List<WeaponDamage> weaponDamagesList = new ArrayList<>(List.of(weaponDamage));
		HashMap<Integer, List<WeaponDamage>> hashMap = new HashMap<>();
		hashMap.put(45, weaponDamagesList);

		RangedWeapon rangedWeapon = RangedWeapon.builder()
			.weaponDamageByLevel(hashMap)
			.magazineSize(12)
			.reloadTime(0)
			.fireRate(43)
			.build();

		loadout.setWeapon(rangedWeapon);

		/*
			without additional modifiers
			DAMAGE PER SHOT: 28.0
			MAGAZINE SIZE: 12
			FIRERATE: 43
			RELOADTIME: 0

			//SHOTS PER SECOND = 43/10 = 4.3.
			//TIME TO USE UP MAGAZINE = 12 / 4.3 = 2.79
			//TOTAL CYCLE = 2.79 + 0 = 4.76
			//DAMAGE PER CYCLE = 28.0 * 12 = 336
			//DAMAGE PER SECOND WITHOUT RELOAD = 120.4
			//DAMAGE PER SECOND WITH RELOAD = 120.4
		 */

		DPSDetails dpsDetails = new DPSDetails(1);
		double dps = rangedDamageCalculator.calculateDPSWithReload(28, 0, loadout, dpsDetails);

		assertEquals(120.4, super.round(dps));
		assertEquals(4.3, dpsDetails.getShotsPerSecond());
		assertEquals(2.79, super.round(dpsDetails.getTimeToEmptyMagazine()));
	}

	@Test
	public void reloadCalculationTest_WithFireRateAsZero() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		WeaponDamage weaponDamage = new WeaponDamage(DamageType.PHYSICAL, 28.0, 0);
		List<WeaponDamage> weaponDamagesList = new ArrayList<>(List.of(weaponDamage));
		HashMap<Integer, List<WeaponDamage>> hashMap = new HashMap<>();
		hashMap.put(45, weaponDamagesList);

		RangedWeapon rangedWeapon = RangedWeapon.builder()
			.weaponDamageByLevel(hashMap)
			.magazineSize(12)
			.reloadTime(1.97)
			.fireRate(0)
			.build();

		loadout.setWeapon(rangedWeapon);

		/*
			without additional modifiers
			DAMAGE PER SHOT: 28.0
			MAGAZINE SIZE: 12
			FIRERATE: 0
			RELOADTIME: 1.97

			//SHOTS PER SECOND = 0/10 = 0.
			//TIME TO USE UP MAGAZINE = 0 / 4.3 = 0
			//TOTAL CYCLE = 0 + 1.97 = 1.97
			//DAMAGE PER CYCLE = 28.0 * 12 = 336
			//DAMAGE PER SECOND WITHOUT RELOAD = 120.4
			//DAMAGE PER SECOND WITH RELOAD = 120.4
		 */

		DPSDetails dpsDetails = new DPSDetails(1);
		double dps = rangedDamageCalculator.calculateDPSWithReload(28, 0, loadout, dpsDetails);

		assertEquals(0, super.round(dps));
		assertEquals(0, dpsDetails.getShotsPerSecond());
		assertEquals(0, super.round(dpsDetails.getTimeToEmptyMagazine()));
	}

	@Test
	public void reloadCalculationTest_WithMeleeWeapon() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		WeaponDamage weaponDamage = new WeaponDamage(DamageType.PHYSICAL, 28.0, 0);
		List<WeaponDamage> weaponDamagesList = new ArrayList<>(List.of(weaponDamage));
		HashMap<Integer, List<WeaponDamage>> hashMap = new HashMap<>();
		hashMap.put(45, weaponDamagesList);

		MeleeWeapon meleeWeapon = MeleeWeapon.builder()
			.weaponDamageByLevel(hashMap)
			.build();

		loadout.setWeapon(meleeWeapon);

		DPSDetails dpsDetails = new DPSDetails(1);
		double dps = rangedDamageCalculator.calculateDPSWithReload(28, 0, loadout, dpsDetails);

		assertEquals(0, super.round(dps));
		assertEquals(0, dpsDetails.getShotsPerSecond());
		assertEquals(0, super.round(dpsDetails.getTimeToEmptyMagazine()));
	}

	//using the calculator
	@Test
	public void reloadCalculationTest_WithCalculator() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		//setting weapon type to 10MM Pistol.
		WeaponDamage weaponDamage = new WeaponDamage(DamageType.PHYSICAL, 28.0, 0);
		WeaponDamage dotDamage = new WeaponDamage(DamageType.FIRE, 9.0, 3);
		List<WeaponDamage> weaponDamagesList = new ArrayList<>(Arrays.asList(weaponDamage, dotDamage));
		HashMap<Integer, List<WeaponDamage>> hashMap = new HashMap<>();
		hashMap.put(45, weaponDamagesList);

		RangedWeapon rangedWeapon = RangedWeapon.builder()
			.weaponDamageByLevel(hashMap)
			.magazineSize(12)
			.reloadTime(1.97)
			.fireRate(43)
			.build();

		loadout.setWeapon(rangedWeapon);

		/*
			without additional modifiers
			DAMAGE PER SHOT: 28.0
			MAGAZINE SIZE: 12
			FIRERATE: 43
			RELOADTIME: 1.97

			STANDARD
			SHOTS PER SECOND = 43/10 = 4.3.
			TIME TO USE UP MAGAZINE = 12 / 4.3 = 2.79
			TOTAL CYCLE = 2.79 + 1.97 = 4.76
			DAMAGE PER CYCLE = 28.0 * 12 = 336
			DAMAGE PER SECOND WITHOUT RELOAD = 336 / 2.79 = 120.4
			DAMAGE PER SECOND WITH RELOAD = 336 / 4.76 = 70.58

			DOT
			DAMAGE OVER TIME = 9 / 3 = 3
			DAMAGE WHILE RELOADING = 1.97 = 1 = 3.
			DAMAGE PER CYCLE = 3 * 12 = 36 + 3 = 39
			DAMAGE PER SECOND WITHOUT RELOAD = 39 / 2.79 = 13.97
			DAMAGE PER SECOND WITH RELOAD = 39 / 4.76 = 8.19

		 */

		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);

		assertEquals(70.6, super.round(dpsDetails.getDamagePerSecond().get(DamageType.PHYSICAL)));
		assertEquals(8.2, super.round(dpsDetails.getDamagePerSecond().get(DamageType.FIRE)));

		assertEquals(4.3, dpsDetails.getShotsPerSecond());
		assertEquals(2.79, super.round(dpsDetails.getTimeToEmptyMagazine()));

		assertEquals(78.8, super.round(dpsDetails.getTotalDamagePerSecond()));
	}

}

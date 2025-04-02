package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.ActionPointsCalculator;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ActionPointsCalculationTest extends BaseTestClass
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	ActionPointsCalculator actionPointsCalculator;

	String _10MMPISTOL;
	String SQUIRRELONASTICK;
	String COOKEDSOFTSHELLMEAT;
	String CALIBRATE;


	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		SQUIRRELONASTICK = jsonIDMapper.getIDFromFileName("SQUIRRELONASTICK");
		COOKEDSOFTSHELLMEAT = jsonIDMapper.getIDFromFileName("COOKEDSOFTSHELLMEAT");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
	}

	@Test
	public void testAPCalculation_withRangedWeapon() throws IOException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);

		RangedWeapon rangedWeapon = (RangedWeapon) loadout.getWeapon();

		//setting base to 10
		loadout.getPlayer().getSpecials().setSpecial(Specials.AGILITY, 10);
		//this should update the players AP
		loadout.getPlayer().setMaxAP(0,0);

		//AP PER SHOT = receiver modifiers base rate of 20 by 0.5 (10 AP per shot)
		//MAX AP = 60 + (10 * Agility) = 160 (without modifiers) - 5 = 155
		double attacksPerSecond = (double) (rangedWeapon.getFireRate() + rangedWeapon.getReceiver().fireRateChange()) / 10;
		//Attacks per second = (43 + 32) / 10 = 7.5

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//AP Duration = 155 / (10 * 7.5) = 2.07

		double apDuration = actionPointsCalculator.calculateAPDuration(attacksPerSecond, loadout, dpsDetails);
		assertEquals(2.07, round(apDuration));
	}

	@Test
	public void testAPCalculation_withRangedWeapon_withModifiers() throws IOException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		weaponManager.modifyWeapon(CALIBRATE, ModType.RECEIVER, loadout);
		RangedWeapon rangedWeapon = (RangedWeapon) loadout.getWeapon();

		//setting base to 10
		loadout.getPlayer().getSpecials().setSpecial(Specials.AGILITY, 15);
		//this should update the players AP
		consumableManager.addConsumable(SQUIRRELONASTICK, loadout);
		consumableManager.addConsumable(COOKEDSOFTSHELLMEAT, loadout);

		//AP PER SHOT = receiver modifiers base rate of 20 by 0 (20 AP per shot)
		//maxAP = (60 + 10 * (specials.getAgility() + bonusAgility)) + bonusAP;
		//MAX AP = 60 + 10 * (Agility + bonuses) + bonusAP = 260 - 5 = 255
		double attacksPerSecond = (double) (rangedWeapon.getFireRate() + rangedWeapon.getReceiver().fireRateChange()) / 10;
		//Attacks per second = (43) / 10 = 4.3

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//AP Duration = 255 / (20 * 4.3) = 2.97

		double apDuration = actionPointsCalculator.calculateAPDuration(attacksPerSecond, loadout, dpsDetails);
		assertEquals(2.97, round(apDuration));
	}

	@Test
	public void testAPCalculation_withRangedWeapon_withNullWeapon() throws IOException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting base to 10
		loadout.getPlayer().getSpecials().setSpecial(Specials.AGILITY, 10);
		//this should update the players AP
		loadout.getPlayer().setMaxAP(0,0);

		//AP PER SHOT = 0
		//maxAP = (60 + 10 * (specials.getAgility() + bonusAgility)) + bonusAP;
		//MAX AP = 60 + 10 * (10 + 0) + 0 = 160 - 5 = 155
		double attacksPerSecond = 4.3;
		//Attacks per second = (43) / 10 = 4.3

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//AP Duration = 155 / (0 * 4.3) = Should result in 0

		double apDuration = actionPointsCalculator.calculateAPDuration(attacksPerSecond, loadout, dpsDetails);
		assertEquals(0, round(apDuration));
	}

	@Test
	public void testAPCalculation_withRangedWeapon_maxApIsZero() throws IOException
	{
		DPSDetails dpsDetails = new DPSDetails(1);
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		RangedWeapon rangedWeapon = (RangedWeapon) loadout.getWeapon();

		//setting base to 1
		loadout.getPlayer().getSpecials().setSpecial(Specials.AGILITY, 1);
		//this should update the players AP while setting the AP to 0
		loadout.getPlayer().setMaxAP(-1,-55);

		//AP PER SHOT = receiver modifiers base rate of 20 by 0.5 (10 AP per shot)
		//maxAP = (60 + 10 * (specials.getAgility() + bonusAgility)) + bonusAP;
		//MAX AP = 60 + 10 * (1 + -1) + -55 = 5 - 5 = 0
		double attacksPerSecond = (double) (rangedWeapon.getFireRate() + rangedWeapon.getReceiver().fireRateChange()) / 10;
		//Attacks per second = (43 + 32) / 10 = 7.5

		//AP Duration = Max AP / (AP PER SHOT * Attacks Per Second)
		//AP Duration = 0 / (10 * 7.5) = Should result in 0

		double apDuration = actionPointsCalculator.calculateAPDuration(attacksPerSecond, loadout, dpsDetails);
		assertEquals(0, round(apDuration));
	}
}

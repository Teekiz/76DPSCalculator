package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.ActionPointsCalculator;
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
public class ActionPointsCalculationTest extends BaseTestClass
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ActionPointsCalculator actionPointsCalculator;

	String _10MMPISTOL;
	String SQUIRRELONASTICK;


	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		SQUIRRELONASTICK = jsonIDMapper.getIDFromFileName("SQUIRRELONASTICK");
	}

	//standard duration
	@Test
	public void testAPCalculation_withStandardLoadout() throws IOException
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


	//weapon is null

	//with modifiers
	//maxAP is 0
}

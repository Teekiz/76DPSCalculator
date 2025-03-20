package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.OutgoingDamage.BonusDamageService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MiscDamageBonuses.SneakBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SneakCalculationTest extends BaseTestClass
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
	BonusDamageService calculator;
	@Autowired
	SneakBonusCalculationService sneakBonusCalculationService;

	@BeforeEach
	public void setUp()
	{
		perkManager.setIgnoreSpecialRestrictions(true);
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@AfterEach
	public void cleanUp()
	{
		perkManager.setIgnoreSpecialRestrictions(false);
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testSneak_WithRangedWeapon_WithRangedPerks() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon("WEAPONS1", loadout);
		perkManager.addPerk("PERKS7", loadout);
		perkManager.changePerkRank("PERKS7", 2, loadout);

		//this should result in a sneak bonus of 1.3x (base 1 + 30% from perk)
		assertEquals(1.3 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,dpsDetails));
	}

	@Test
	public void testSneak_WithMeleeWeapon_WithMeleePerks() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon("WEAPONS2", loadout);
		perkManager.addPerk("PERKS10", loadout);
		perkManager.changePerkRank("PERKS10", 3, loadout);

		//this should result in a sneak bonus of 1.9x (base 1 + 90% from perk)
		assertEquals(1.9 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,dpsDetails));
	}

	@Test
	public void testSneak_WithBothPerks() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon("WEAPONS1", loadout);
		perkManager.addPerk("PERKS7", loadout);
		perkManager.changePerkRank("PERKS7", 3, loadout); //0.5
		perkManager.addPerk("PERKS10", loadout);
		perkManager.changePerkRank("PERKS10", 2, loadout); //0.6

		//this should result in a sneak bonus of 1.5x (base 1 + 50% from perk)
		assertEquals(1.5 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,dpsDetails));

		weaponManager.setWeapon("WEAPONS2", loadout);
		assertEquals(1.6 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,dpsDetails));
	}

	@Test
	public void testSneak_WithNoPerks() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon("WEAPONS1", loadout);

		//this should result in a sneak bonus of 1x
		assertEquals(1.0 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,dpsDetails));
	}

	@Test
	public void testSneakFromBonusCalculator() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);
		loadout.getPlayer().setSneaking(true);

		weaponManager.setWeapon("WEAPONS1", loadout);
		perkManager.addPerk("PERKS7", loadout);
		perkManager.changePerkRank("PERKS7", 3, loadout); //0.5

		consumableManager.addConsumable("CONSUMABLES2", loadout);//BALLISTICBOCK - 0.15

		//with the additional perks (0.15), and the sneak perk (0.5) with receiver (-0.1), this should result in (1.15 + 1.5 - 0.1 = 2.55 bonus damage)
		assertEquals(2.55, calculator.calculateBonusDamage(loadout, dpsDetails));
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
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
	DamageCalculationService calculator;
	@Autowired
	SneakBonusCalculationService sneakBonusCalculationService;

	//TEST OBJECT IDS
	String _10MMPISTOL;
	String COVERT_OPERATIVE;
	String ASSAULTRONBLADE;
	String NINJA;
	String BALLISTICBOCK;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		COVERT_OPERATIVE = jsonIDMapper.getIDFromFileName("COVERT_OPERATIVE");
		ASSAULTRONBLADE = jsonIDMapper.getIDFromFileName("ASSAULTRONBLADE");
		NINJA = jsonIDMapper.getIDFromFileName("NINJA");
		BALLISTICBOCK = jsonIDMapper.getIDFromFileName("BALLISTICBOCK");
	}

	@BeforeEach
	public void setUp()
	{
		perkManager.setIgnoreSpecialRestrictions(true);
	}

	@AfterEach
	public void cleanUp()
	{
		perkManager.setIgnoreSpecialRestrictions(false);
	}

	@Test
	public void testSneak_WithRangedWeapon_WithRangedPerks() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon(_10MMPISTOL, loadout);
		perkManager.addPerk(COVERT_OPERATIVE, loadout);
		perkManager.changePerkRank(COVERT_OPERATIVE, 2, loadout);

		//this should result in a sneak bonus of 1.3x (base 1 + 30% from perk)
		assertEquals(1.3 ,sneakBonusCalculationService.getSneakDamageBonus(loadout, null, dpsDetails));
	}

	@Test
	public void testSneak_WithMeleeWeapon_WithMeleePerks() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);
		perkManager.addPerk(NINJA, loadout);
		perkManager.changePerkRank(NINJA, 3, loadout);

		//this should result in a sneak bonus of 1.9x (base 1 + 90% from perk)
		assertEquals(1.9 ,sneakBonusCalculationService.getSneakDamageBonus(loadout,null, dpsDetails));
	}

	@Test
	public void testSneak_WithBothPerks() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon(_10MMPISTOL, loadout);
		perkManager.addPerk(COVERT_OPERATIVE, loadout);
		perkManager.changePerkRank(COVERT_OPERATIVE, 3, loadout); //0.5
		perkManager.addPerk(NINJA, loadout);
		perkManager.changePerkRank(NINJA, 2, loadout); //0.6

		//this should result in a sneak bonus of 1.5x (base 1 + 50% from perk)
		assertEquals(1.5 ,sneakBonusCalculationService.getSneakDamageBonus(loadout, null, dpsDetails));

		weaponManager.setWeapon(ASSAULTRONBLADE, loadout);
		assertEquals(1.6 ,sneakBonusCalculationService.getSneakDamageBonus(loadout, null, dpsDetails));
	}

	@Test
	public void testSneak_WithNoPerks() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		DPSDetails dpsDetails = new DPSDetails(1);

		weaponManager.setWeapon(_10MMPISTOL, loadout);

		//this should result in a sneak bonus of 1x
		assertEquals(1.0, sneakBonusCalculationService.getSneakDamageBonus(loadout,null, dpsDetails));
	}

	@Test
	public void testSneakFromBonusCalculator() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		loadout.getPlayer().setSneaking(true);

		weaponManager.setWeapon(_10MMPISTOL, loadout);
		perkManager.addPerk(COVERT_OPERATIVE, loadout);
		perkManager.changePerkRank(COVERT_OPERATIVE, 3, loadout); //0.5

		consumableManager.addConsumable(BALLISTICBOCK, loadout);//BALLISTICBOCK - 0.15

		//with the additional perks (0.15), and the sneak perk (0.5) with receiver (-0.1), this should result in (1.15 + 1.5 - 0.1 = 2.55 bonus damage)
		//28.0 * 2.55 = 71.4
		DPSDetails dpsDetails = calculator.calculateOutgoingDamage(loadout);
		assertEquals(71.4, dpsDetails.getDamageDetailsRecord(DamageType.PHYSICAL).getDamagePerShot());
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
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
	ConsumableManager consumableManager;
	@Autowired
	DamageCalculationService calculator;

	@Test
	public void testWithBaseDamage() throws IOException
	{
		log.debug("{}Running test - testWithBaseDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//setting the conditions for the two base weapons
		weaponManager.setWeapon("WEAPONS2", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE

		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout));

		//removing the perk should reduce the damage by 20%
		perkManager.removePerk("PERKS5", loadout);//TESTEVENT
		assertEquals(33.6, calculator.calculateOutgoingDamage(loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testMultiplicativeDamage() throws IOException
	{
		log.debug("{}Running test - testMultiplicativeDamage in CalculationServicesTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		weaponManager.setWeapon("WEAPONS2", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALIBRATE
		perkManager.addPerk("PERKS5", loadout);//TESTEVENT
		consumableManager.addConsumable("CONSUMABLES8", loadout);//TESTEVENTTWO
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout));

		//level 1 tenderizer should add 5% extra damage on top of the existing damage
		//39.2 * (1 + 0.05) = 41.16 (41.2)
		perkManager.addPerk("perks3", loadout);//TENDERIZER
		assertEquals(41.2, calculator.calculateOutgoingDamage(loadout));
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

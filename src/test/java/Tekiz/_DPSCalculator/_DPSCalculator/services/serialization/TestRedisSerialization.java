package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class TestRedisSerialization extends BaseTestClass
{
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	MutationManager mutationManager;

	@Test
	public void testRedisSerialization() throws IOException
	{
		log.debug("{}Running test - testRedisSerialization in TestRedisSerialization.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("WEAPONS2", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("MODRECEIVERS2", ModType.RECEIVER, loadout);//CALLIBRATE
		consumableManager.addConsumable("CONSUMABLES3", loadout);//FURY
		consumableManager.addConsumable("CONSUMABLES4", loadout);//BALLISTICBOCK
		perkManager.addPerk("PERKS1", loadout);//GUNSLINGER
		perkManager.addPerk("PERKS2", loadout);//STRANGEINNUMBERS
		mutationManager.addMutation("MUTATIONS1", loadout);//ADRENALREACTION

		loadout = null;

		Loadout newLoadout = loadoutManager.getLoadout(1);
		assertNotNull(newLoadout);

		assertNotNull(newLoadout.getWeapon());
		assertEquals("Test 10mm pistol", newLoadout.getWeapon().getWeaponName());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

}

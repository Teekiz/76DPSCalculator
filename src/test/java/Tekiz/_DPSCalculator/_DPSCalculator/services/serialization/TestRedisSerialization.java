package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class TestRedisSerialization
{
	@Autowired
	UserLoadoutTracker userLoadoutTracker;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	MutationManager mutationManager;
	@Autowired
	LoadoutManager loadoutManager;

	@Test
	public void testRedisSerialization() throws IOException
	{
		log.debug("{}Running test - testRedisSerialization in TestRedisSerialization.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL
		weaponManager.modifyWeapon("modRe2", ModType.RECEIVER, loadout);//CALLIBRATE
		consumableManager.addConsumable("consu3", loadout);//FURY
		consumableManager.addConsumable("consu4", loadout);//BALLISTICBOCK
		perkManager.addPerk("perks1", loadout);//GUNSLINGER
		perkManager.addPerk("perks2", loadout);//STRANGEINNUMBERS
		mutationManager.addMutation("mutat1", loadout);//ADRENALREACTION

		loadout = null;

		Loadout newLoadout = loadoutManager.getLoadout(1);
		assertNotNull(newLoadout);

		assertNotNull(newLoadout.getWeapon());
		assertEquals("Test 10mm pistol", newLoadout.getWeapon().getWeaponName());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

}

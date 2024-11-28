package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class ModifierExpressionServicesTest extends BaseTestClass
{
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PlayerManager playerManager;
	@Autowired
	MutationManager mutationManager;
	@Autowired
	DamageCalculationService calculator;

	@Test
	public void testMutationEffect() throws IOException
	{
		log.debug("{}Running test - testMutationEffect in ModifierExpressionServiceTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("WEAPONS2", loadout);//10MMPISTOL
		playerManager.getPlayer(loadout).setCurrentHP(125.0);

		mutationManager.addMutation("MUTATIONS1", loadout);//ADRENALREACTION

			//hp is set to 125, so it sound return 0.19 additional damage
			//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 30.5
			//28 * (1 + 0.19 - 0.1) = 30.5 (30.52)

		assertEquals(30.5, calculator.calculateOutgoingDamage(loadout));

			//(33 / 200) * 100 = 16.5% hp left. This results in a damage boost of 0.50.
			//28 * (1 + 0.5 - 0.1) = 39.2

		playerManager.getPlayer(loadout).setCurrentHP(33.0);
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout));

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

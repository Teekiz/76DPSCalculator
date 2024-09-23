package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.config.TestInitializer;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(initializers = TestInitializer.class)
public class AdditionalContextServicesTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	DamageCalculationService calculator;

	@Test
	public void testMutationEffect() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");
		loadout.getPlayerManager().getPlayer().setCurrentHP(125.0);

		loadout.getMutationManager().addMutation("ADRENALREACTION");

		/*
			hp is set to half, so it sound return 0.31 additional damage
			level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 33.9
			28 * (1 + 0.31 - 0.1) = 33.88 (33.9)
		 */
		assertEquals(33.9, calculator.calculateOutgoingDamage(loadoutManager.getLoadout()));

		/*
			(33 / 250) * 100 = 13.2% hp left. This results in a damage boost of 0.50.
			28 * (1 + 0.5 - 0.1) = 39.2
		 */

		loadout.getPlayerManager().getPlayer().setCurrentHP(33.0);
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.deleteAllLoadouts();
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ModifierExpressionServicesTest
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	DamageCalculationService calculator;

	@Test
	public void testMutationEffect() throws IOException
	{
		loadoutManager.deleteAllLoadouts();

		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");
		loadout.getPlayerManager().getPlayer().setCurrentHP(125.0);

		loadout.getMutationManager().addMutation("ADRENALREACTION");

		/*
			hp is set to 125, so it sound return 0.19 additional damage
			level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 30.5
			28 * (1 + 0.19 - 0.1) = 30.5 (30.52)
		 */
		assertEquals(30.5, calculator.calculateOutgoingDamage(loadoutManager.getLoadout()));

		/*
			(33 / 200) * 100 = 16.5% hp left. This results in a damage boost of 0.50.
			28 * (1 + 0.5 - 0.1) = 39.2
		 */

		loadout.getPlayerManager().getPlayer().setCurrentHP(33.0);
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.deleteAllLoadouts();
	}
}

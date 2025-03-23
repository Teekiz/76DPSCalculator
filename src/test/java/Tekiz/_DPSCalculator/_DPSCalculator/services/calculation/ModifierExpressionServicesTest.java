package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

	//TEST OBJECT IDS
	String _10MMPISTOL;
	String ADRENALREACTION;

	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		ADRENALREACTION = jsonIDMapper.getIDFromFileName("ADRENALREACTION");
	}

	@Test
	public void testMutationEffect() throws IOException
	{
		log.debug("{}Running test - testMutationEffect in ModifierExpressionServiceTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		playerManager.getPlayer(loadout).setCurrentHP(125.0);

		mutationManager.addMutation(ADRENALREACTION, loadout);//ADRENALREACTION

			//hp is set to 125, so it sound return 0.19 additional damage
			//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 30.5
			//28 * (1 + 0.19 - 0.1) = 30.5 (30.52)

		assertEquals(30.5, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

			//(33 / 200) * 100 = 16.5% hp left. This results in a damage boost of 0.50.
			//28 * (1 + 0.5 - 0.1) = 39.2

		playerManager.getPlayer(loadout).setCurrentHP(33.0);
		assertEquals(39.2, calculator.calculateOutgoingDamage(loadout).getTotalDamagePerShot());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

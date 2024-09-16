package Tekiz._DPSCalculator._DPSCalculator.services.scope;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class ScopeTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Test
	public void testLoadoutScope()
	{
		loadoutManager.setActiveLoadout(1);
		Loadout loadout1 = loadoutManager.getLoadout();

		loadoutManager.setActiveLoadout(2);
		Loadout loadout2 = loadoutManager.getLoadout();

		assertNotSame(loadout1, loadout2);

		loadoutManager.setActiveLoadout(1);
		Loadout loadout3 = loadoutManager.getLoadout();

		assertSame(loadout1, loadout3);
		assertNotSame(loadout1.getPerkManager(), loadout2.getPerkManager());

		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testDeletingScopes()
	{
		loadoutManager.setActiveLoadout(1);
		Loadout loadout1 = loadoutManager.getLoadout();

		loadoutManager.setActiveLoadout(2);
		Loadout loadout2 = loadoutManager.getLoadout();

		assertNotSame(loadout1, loadout2);

		loadoutManager.setActiveLoadout(1);
		Loadout loadout3 = loadoutManager.getLoadout();

		assertSame(loadout1, loadout3);
		assertNotSame(loadout1.getPerkManager(), loadout2.getPerkManager());

		loadoutManager.deleteLoadout(loadoutManager.getLoadout());
		loadoutManager.setActiveLoadout(2);
		loadoutManager.deleteLoadout(loadoutManager.getLoadout());


	}
}

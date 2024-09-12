package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json", "perk.data.file.path=src/test/resources/data/perkData/testPerks.json", "receivers.data.file.path=src/test/resources/data/weaponData/testReceivers.json"})
public class WeaponChangedEventTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Test
	public void testExpression() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getPerkManager().addPerk("HEAVYGUNNER");
		assertFalse(loadout.getPerkManager().getPerks().isEmpty());

		//todo - make a manager for modifiers - redo perk logic so that it can automatically save the expression.
		assertNotNull(loadout.getPerkManager().getPerks().iterator().next().getCondition());
		loadoutManager.deleteAllLoadouts();
	}
}

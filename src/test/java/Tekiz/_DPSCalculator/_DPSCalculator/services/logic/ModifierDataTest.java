package Tekiz._DPSCalculator._DPSCalculator.services.logic;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
	"perk.data.file.path=src/test/resources/data/perkData/testPerks.json",
	"receivers.data.file.path=src/test/resources/data/weaponData/testReceivers.json",
	"consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json"})
public class ModifierDataTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Test
	public void testPerkModifier() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getWeaponManager().setWeapon("10MMPISTOL");
		loadout.getPerkManager().addPerk("TESTMODIFIER");
		Double bonus = loadout.getModifierManager().getMiscModifiers().getAdditiveWeaponDamageBonus();
		assertEquals(1.20, bonus);
		loadoutManager.deleteAllLoadouts();
	}

}

package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnemyManagerTest extends BaseTestClass
{
	@Autowired
	EnemyManager enemyManager;

	@Test
	public void canEnemyBeChanged() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		enemyManager.changeEnemy("ENEMIES1", loadout);
		assertEquals("Feral ghoul", loadout.getEnemy().getName());

		enemyManager.changeEnemy("ENEMIES3", loadout);
		assertEquals("Super mutant", loadout.getEnemy().getName());
	}

	@Test
	public void canEnemyBeChanged_WithInvalidID() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		enemyManager.changeEnemy("ENEMIES1", loadout);
		assertEquals("Feral ghoul", loadout.getEnemy().getName());

		enemyManager.changeEnemy("invalidEnemyID", loadout);
		//it should not have changed.
		assertEquals("Feral ghoul", loadout.getEnemy().getName());
	}
}

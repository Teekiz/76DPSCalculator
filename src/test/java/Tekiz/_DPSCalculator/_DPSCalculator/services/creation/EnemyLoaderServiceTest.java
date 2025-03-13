package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnemyLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;

	//todo - add this test
	@Test
	void testLoadEnemy_ID() throws IOException
	{
		String enemyID = "ENEMIES1";
		Enemy enemy = dataLoaderService.loadData(enemyID, Enemy.class, null);
		assertNotNull(enemy);
		assertEquals(EnemyType.GHOUL, enemy.getEnemyType());
	}

	@Test
	void testLoadEnemy_NAME() throws IOException
	{
		String enemyName = "FERAL_GHOUL";
		Enemy enemy = dataLoaderService.loadDataByName(enemyName, Enemy.class, null);
		assertNotNull(enemy);
		assertEquals(EnemyType.GHOUL, enemy.getEnemyType());
	}

	@Test
	void testLoadAllEnemies() throws IOException
	{
		List<Enemy> enemies = dataLoaderService.loadAllData("enemies", Enemy.class, null);
		assertNotNull(enemies);
		assertEquals("Mr Gutsy", enemies.get(0).getName());
		assertEquals(EnemyType.SUPER_MUTANT, enemies.get(1).getEnemyType());
		assertEquals(1000000000, enemies.get(1).getArmourResistance().getRadiationResistance());
	}
}

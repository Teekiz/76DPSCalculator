package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnemyManagerTest extends BaseTestClass
{
	@Autowired
	EnemyManager enemyManager;

	String FERAL_GHOUL;
	String SUPER_MUTANT;

	@BeforeEach
	public void initializeVariables()
	{
		FERAL_GHOUL = jsonIDMapper.getIDFromFileName("FERAL_GHOUL");
		SUPER_MUTANT = jsonIDMapper.getIDFromFileName("SUPER_MUTANT");
	}

	@Test
	public void canEnemyBeChanged() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		enemyManager.changeEnemy(FERAL_GHOUL, loadout);
		assertEquals("Feral ghoul", loadout.getEnemy().getName());

		enemyManager.changeEnemy(SUPER_MUTANT, loadout);
		assertEquals("Super mutant", loadout.getEnemy().getName());
	}

	@Test
	public void canEnemyBeChanged_WithInvalidID() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		enemyManager.changeEnemy(FERAL_GHOUL, loadout);
		assertEquals("Feral ghoul", loadout.getEnemy().getName());

		assertThrows(ResourceNotFoundException.class, () -> enemyManager.changeEnemy("invalidEnemyID", loadout));
		//it should not have changed.
		assertEquals("Feral ghoul", loadout.getEnemy().getName());
	}
}

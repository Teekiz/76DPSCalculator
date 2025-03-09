package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnemyManager
{
	private final DataLoaderService dataLoaderService;

	@Autowired
	public EnemyManager(DataLoaderService dataLoaderService)
	{
		this.dataLoaderService = dataLoaderService;
	}

	@SaveLoadout
	public void changeEnemy(String enemyID, Loadout loadout) throws IOException
	{
		Enemy enemy = dataLoaderService.loadData(enemyID, Enemy.class, null);

		if (enemy == null) {
			return;
		}

		log.debug("Changing enemy to {} in loadout {}.", enemy.getName(), loadout.getLoadoutID());
		loadout.setEnemy(enemy);
	}
}

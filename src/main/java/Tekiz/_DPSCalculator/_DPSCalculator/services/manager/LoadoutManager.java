package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.LoadoutFactory;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class LoadoutManager
{
	//todo - update this
	private int activeLoadout = 1;
	private final Map<Integer, Loadout> loadoutMap = new HashMap<>();

	@Autowired
	private LoadoutFactory loadoutFactory;

	@Autowired
	private ApplicationContext context;

	public Loadout getLoadout()
	{
		return loadoutMap.computeIfAbsent(activeLoadout, num -> loadoutFactory.createLoadout(num));
	}

	@PreDestroy
	public void deleteAllLoadouts()
	{
		for (Loadout loadout : loadoutMap.values())
		{
			loadout.getWeaponManager().clear();
			loadout.getPerkManager().clear();
			loadout.getConsumableManager().clear();
			loadout.getArmourManager().clear();
			loadout.getPlayerManager().clear();
			loadout.getEnvironmentManager().clear();
		}
		loadoutMap.clear();
	}
}

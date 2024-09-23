package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScope;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class LoadoutManager
{
	@Setter
	private int activeLoadout = 1;
	@Autowired
	private ApplicationContext context;

	private final Map<Integer, Loadout> loadoutMap = new HashMap<>();

	public Loadout getLoadout()
	{
		return loadoutMap.computeIfAbsent(activeLoadout,
			id ->
			{
				LoadoutScope.loadoutIdStorage.set(id);
				return context.getBean(Loadout.class);
			});
	}

	public void deleteLoadout(Loadout loadout)
	{
		//makes sure that the ID being used is the correctly set.
		int id = loadout.getLoadoutID();
		LoadoutScope.loadoutIdStorage.set(id);
		loadoutMap.remove(id);
		context.getBean(LoadoutScope.class).remove(String.valueOf(id));
	}

	@PreDestroy
	public void deleteAllLoadouts() {
		for (Loadout loadout : loadoutMap.values()) {

			//makes sure that the ID being used is the correctly set.
			int id = loadout.getLoadoutID();
			LoadoutScope.loadoutIdStorage.set(id);
			context.getBean(LoadoutScope.class).remove(String.valueOf(id));
		}
		loadoutMap.clear();
	}
}

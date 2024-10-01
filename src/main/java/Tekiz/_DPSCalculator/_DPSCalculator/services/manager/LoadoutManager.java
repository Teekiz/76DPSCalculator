package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScope;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * A service used to manage {@link Loadout} objects.
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoadoutManager
{
	@Setter
	private int activeLoadout = 1;
	@Autowired
	private ApplicationContext context;
	private static final Logger logger = LoggerFactory.getLogger(LoadoutManager.class);
	private final Map<Integer, Loadout> loadoutMap = new HashMap<>();

	/**
	 * Retrieves the current active {@link Loadout} based on the ID of the active loadout.
	 * If no loadout exists for the given ID, a new one is created and added to the loadout map.
	 *
	 * @return The {@link Loadout} associated with the current active loadout ID.
	 */
	public Loadout getLoadout()
	{
		logger.info("New getLoadout() request made for id {}.", activeLoadout);
		return loadoutMap.computeIfAbsent(activeLoadout,
			id ->
			{
				LoadoutScope.loadoutIdStorage.set(id);
				return context.getBean(Loadout.class);
			});
	}

	/**
	 * Deletes the given {@link Loadout} from the loadout map and its associated scoped beans.
	 * This also clears the loadout from the {@link LoadoutScope}.
	 *
	 * @param loadout The loadout to be deleted.
	 */
	public void deleteLoadout(Loadout loadout)
	{
		//makes sure that the ID being used is the correctly set.
		int id = loadout.getLoadoutID();
		logger.info("New deleteLoadout() request made for id {}.", id);
		LoadoutScope.loadoutIdStorage.set(id);
		loadoutMap.remove(id);
		context.getBean(LoadoutScope.class).remove(String.valueOf(id));
	}

	/**
	 * Deletes all loadouts from the loadout map and clears the associated scoped beans from the {@link LoadoutScope}.
	 * This method is called before the service is destroyed to clean up the loadouts.
	 */
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

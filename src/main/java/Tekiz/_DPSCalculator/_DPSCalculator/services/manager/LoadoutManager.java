package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.session.RedisLoadoutService;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Loadout} objects.
 */
@Service
@Slf4j
public class LoadoutManager
{
	private final UserLoadoutTracker userLoadoutTracker;
	private final RedisLoadoutService redisLoadoutService;
	private final LoadoutFactory loadoutFactory;

	@Autowired
	public LoadoutManager(UserLoadoutTracker userLoadoutTracker, RedisLoadoutService redisLoadoutService, LoadoutFactory loadoutFactory)
	{
		this.redisLoadoutService = redisLoadoutService;
		this.loadoutFactory = loadoutFactory;
		this.userLoadoutTracker = userLoadoutTracker;
		log.info("Creating new loadout manager.");
	}

	/**
	 * Retrieves the current active {@link Loadout} based on the ID of the active loadout.
	 * If no loadout exists for the given ID, a new one is created and added to the loadout map.
	 *
	 * @return The {@link Loadout} associated with the current active loadout ID.
	 */
	public Loadout getLoadout(int loadoutID)
	{
		String sessionID = userLoadoutTracker.getSessionID();
		Set<Loadout> loadouts = redisLoadoutService.getSessionLoadouts(sessionID);

		return loadouts.stream()
			.filter(l -> l.getLoadoutID() == loadoutID)
			.findFirst()
			.orElseGet(() ->
			{
				log.info("Created new loadout (ID: {}) for session: {}", loadoutID, sessionID);
				Loadout newLoadout = loadoutFactory.createNewLoadout(loadoutID);
				saveActiveLoadout(sessionID, newLoadout);
				return newLoadout;
			});
	}

	/**
	 * Retrieves all loadouts tied to a session key.
	 * @return A {@link Set} of {@link Loadout} objects.
	 */
	public Set<Loadout> getLoadouts()
	{
		String sessionID = userLoadoutTracker.getSessionID();
		return redisLoadoutService.getSessionLoadouts(sessionID);
	}
	public void saveActiveLoadout(String sessionID, Loadout loadout)
	{
		redisLoadoutService.saveLoadout(sessionID, loadout);
	}
	/**
	 * Deletes the given {@link Loadout} from the loadout map and its associated scoped beans.
	 * This also clears the loadout.
	 */
	public void deleteLoadout(String sessionID, int loadoutID)
	{
		redisLoadoutService.deleteSessionLoadout(sessionID, loadoutID);
	}

	/**
	 * Deletes all loadouts from the loadout map and clears the associated scoped beans.
	 * This method is called before the service is destroyed to clean up the loadouts.
	 */
	public void deleteAllLoadouts(String sessionID) {
		redisLoadoutService.deleteAllSessionLoadouts(sessionID);
	}
}

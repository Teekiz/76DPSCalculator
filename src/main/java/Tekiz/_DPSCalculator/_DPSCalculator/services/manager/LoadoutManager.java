package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.session.RedisLoadoutService;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * A service used to manage {@link Loadout} objects.
 */
@Service
@SessionScope
@Slf4j
public class LoadoutManager
{
	@Getter @Setter
	private Integer activeLoadoutNumber = 1;
	private final RedisLoadoutService redisLoadoutService;
	private final LoadoutFactory loadoutFactory;

	@Autowired
	public LoadoutManager(RedisLoadoutService redisLoadoutService, LoadoutFactory loadoutFactory)
	{
		this.redisLoadoutService = redisLoadoutService;
		this.loadoutFactory = loadoutFactory;
		log.info("Creating new loadout manager for session: {}", getSessionID());
	}

	private String getSessionID()
	{
		return RequestContextHolder.currentRequestAttributes().getSessionId();
	}

	/**
	 * Retrieves the current active {@link Loadout} based on the ID of the active loadout.
	 * If no loadout exists for the given ID, a new one is created and added to the loadout map.
	 *
	 * @return The {@link Loadout} associated with the current active loadout ID.
	 */
	public Loadout getActiveLoadout()
	{
		HashMap<Integer, Loadout> loadouts = redisLoadoutService.getSessionLoadouts(getSessionID());
		return loadouts.computeIfAbsent(activeLoadoutNumber, _ ->
			{
				log.info("Created new loadout (ID: {}) for session: {}", activeLoadoutNumber, getSessionID());
				Loadout loadout = loadoutFactory.createNewLoadout();
				redisLoadoutService.saveLoadout(getSessionID(), activeLoadoutNumber, loadout);
				return loadout;
			});
	}
	public void saveActiveLoadout()
	{
		redisLoadoutService.saveLoadout(getSessionID(), getActiveLoadoutNumber(), getActiveLoadout());
	}

	/**
	 * Deletes the given {@link Loadout} from the loadout map and its associated scoped beans.
	 * This also clears the loadout.
	 */
	public void deleteLoadout(int loadoutID)
	{
		redisLoadoutService.deleteSessionLoadout(getSessionID(), loadoutID);
	}

	/**
	 * Deletes all loadouts from the loadout map and clears the associated scoped beans.
	 * This method is called before the service is destroyed to clean up the loadouts.
	 */
	public void deleteAllLoadouts() {
		redisLoadoutService.deleteAllSessionLoadouts(getSessionID());
	}
}

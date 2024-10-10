package Tekiz._DPSCalculator._DPSCalculator.services.session;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * A service used to manage loadout distribution and caching using redis.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RedisLoadoutService
{
	private final RedisTemplate<String, HashMap<Integer, Loadout>> redisTemplate;
	public HashMap<Integer, Loadout> getSessionLoadouts(String sessionID) {
		log.debug("Getting loadouts for session: {}", sessionID);

		HashMap<Integer, Loadout> redisHashmap = redisTemplate.opsForValue().get("session:" + sessionID + ":loadouts");

		if (redisHashmap == null)
		{
			log.info("Could not find loadouts for {}. Creating new loadout map.", sessionID);
			return new HashMap<>();
		}
		return redisHashmap;
	}
	public void saveLoadout(String sessionID, int loadoutID, Loadout loadout)
	{
		redisTemplate.opsForHash().put(sessionID, loadoutID, loadout);
		log.debug("Saving loadouts for session: {}. Loadout: {}.", sessionID, loadoutID);
	}
	public void saveAllLoadoutsInSession(String sessionID, HashMap<Integer, Loadout> loadouts)
	{
		redisTemplate.opsForValue().set("session:" + sessionID + ":loadouts", loadouts);
		log.debug("Saving loadouts for session: {}", sessionID);
	}
	public void deleteSessionLoadout(String sessionID, int loadoutID)
	{
		HashMap<Integer, Loadout> loadouts = getSessionLoadouts(sessionID);

		if (loadouts != null && loadouts.containsKey(loadoutID)) {
			loadouts.remove(loadoutID);
			saveAllLoadoutsInSession(sessionID, loadouts);
			log.debug("Deleted loadout: {} from session: {}", loadoutID, sessionID);
		} else {
			log.error("Tried to delete loadout: {} for session: {} but it does not exist", loadoutID, sessionID);
		}
	}
	public void deleteAllSessionLoadouts(String sessionID)
	{
		redisTemplate.delete("session:" + sessionID + ":loadouts");
		log.debug("Deleted all loadouts for session ID: {}", sessionID);
	}
}

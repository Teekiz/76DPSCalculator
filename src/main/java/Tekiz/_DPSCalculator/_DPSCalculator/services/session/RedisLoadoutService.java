package Tekiz._DPSCalculator._DPSCalculator.services.session;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import java.util.HashSet;
import java.util.Set;
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
	private final RedisTemplate<String, Loadout> redisTemplate;
	public Set<Loadout> getSessionLoadouts(String sessionID) {
		log.debug("Getting loadouts for session: {}", sessionID);

		Set<Loadout> redisZSet = redisTemplate.opsForZSet().range("session:" + sessionID + ":loadouts", 0, -1);

		if (redisZSet == null)
		{
			log.info("Could not find loadouts for {}. Creating new loadout map.", sessionID);
			return new HashSet<>();
		}
		return redisZSet;
	}
	public void saveLoadout(String sessionID, Loadout loadout)
	{
		redisTemplate.opsForZSet().removeRangeByScore("session:" + sessionID + ":loadouts", loadout.getLoadoutID(), loadout.getLoadoutID());
		redisTemplate.opsForZSet().add("session:" + sessionID + ":loadouts", loadout, loadout.getLoadoutID());
		log.debug("Saving loadouts for session: {}. Loadout: {}.", sessionID, loadout.getLoadoutID());
	}
	public void saveAllLoadoutsInSession(String sessionID, Set<Loadout> loadouts)
	{
		redisTemplate.delete("session:" + sessionID + ":loadouts"); // Clear the existing set

		for (Loadout loadout : loadouts) {
			redisTemplate.opsForZSet().add("session:" + sessionID + ":loadouts", loadout, loadout.getLoadoutID());
		}

		log.debug("Saving loadouts for session: {}", sessionID);
	}
	public void deleteSessionLoadout(String sessionID, int loadoutID)
	{
		Loadout loadoutToDelete = getSessionLoadouts(sessionID).stream()
			.filter(loadout -> loadout.getLoadoutID() == loadoutID)
			.findFirst()
			.orElse(null);

		if (loadoutToDelete != null) {
			redisTemplate.opsForZSet().remove("session:" + sessionID + ":loadouts", loadoutToDelete);
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

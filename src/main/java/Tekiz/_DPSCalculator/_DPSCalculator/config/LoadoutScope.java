package Tekiz._DPSCalculator._DPSCalculator.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

@Component
public class LoadoutScope implements Scope {

	//Integer is the ID of the loadout, used to identify the corresponding managers in maps.
	private final Map<Integer, Map<String, Object>> scopedObjects = new ConcurrentHashMap<>(new HashMap<>());
	private final Map<Integer, Map<String, Runnable>> destructionCallBacks = new ConcurrentHashMap<>(new HashMap<>());
	public static final ThreadLocal<Integer> loadoutIdStorage = new ThreadLocal<>();
	private static final Logger logger = LoggerFactory.getLogger(LoadoutScope.class);

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory)
	{
		Integer loadoutID = loadoutIdStorage.get();

		if (loadoutID == null) {
			logger.error("Loadout ID is not set in the thread-local context.");
			throw new IllegalStateException("Loadout ID is not set in the thread-local context.");
		}

		//if the scopedObjects does not contain a matching loadout.
		scopedObjects.computeIfAbsent(loadoutID, id -> new HashMap<>());
		Map<String, Object> scopedObjectMap = scopedObjects.get(loadoutID);

		//if the loadout has does not have a bean of the matching name
		if (scopedObjectMap != null && !scopedObjectMap.containsKey(name))
		{
			Object object = objectFactory.getObject();
			scopedObjectMap.put(name, object);

			logger.debug("Created new object {} in loadout scope for ID {}. Hashcode: {}", name, loadoutID, System.identityHashCode(this));

			destructionCallBacks.computeIfAbsent(loadoutID, id -> new HashMap<>())
				.put(name, () -> {
					if (object instanceof LoadoutScopeClearable)
					{
						((LoadoutScopeClearable) object).clear();
					}
			});
		}

		return scopedObjectMap != null ? scopedObjectMap.get(name) : null;
	}

	public Object remove(String name) {
		Integer loadoutID = loadoutIdStorage.get();
		if (loadoutID == null) {
			logger.error("Loadout ID is not set in the thread-local context.");
			throw new IllegalStateException("Loadout ID is not set in the thread-local context.");
		}

		logger.debug("Removing object {} in loadout scope for ID {}. Hashcode: {}", name, loadoutID, System.identityHashCode(this));

		// Get the scoped objects and destruction callbacks for this loadout
		Map<String, Object> scopedObjectMap = scopedObjects.get(loadoutID);
		Map<String, Runnable> callbackMap = destructionCallBacks.get(loadoutID);

		if (scopedObjectMap != null) {
			// Loop through each object in the scopedObjectMap and run its destruction callback if it exists
			for (String objectName : scopedObjectMap.keySet()) {
				if (callbackMap != null && callbackMap.containsKey(objectName)) {
					Runnable callback = callbackMap.remove(objectName);
					if (callback != null) {
						callback.run();
					}
				}
			}

			// Clear the scoped objects and destruction callbacks for this loadout ID
			scopedObjects.remove(loadoutID);
			destructionCallBacks.remove(loadoutID);

			return scopedObjectMap;
		}

		// No objects in the scope
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback)
	{
		Integer loadoutID = loadoutIdStorage.get();
		if (loadoutID == null) {
			logger.error("Loadout ID is not set in the thread-local context.");
			throw new IllegalStateException("Loadout ID is not set in the thread-local context.");
		}

		destructionCallBacks.computeIfAbsent(loadoutID, k -> new HashMap<>())
			.put(name, callback);
	}

	@Override
	public Object resolveContextualObject(String key)
	{
		return null;
	}

	@Override
	public String getConversationId()
	{
		return null;
	}
}
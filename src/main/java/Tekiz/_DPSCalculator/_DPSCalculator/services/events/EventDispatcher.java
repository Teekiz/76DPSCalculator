package Tekiz._DPSCalculator._DPSCalculator.services.events;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher
{
	private final List<WeaponModifiedListener> weaponModifiedListeners = new ArrayList<>();

	public void registerWeaponModifiedListener(WeaponModifiedListener listener)
	{
		weaponModifiedListeners.add(listener);
	}

	public void dispatchWeaponModifiedEvent(WeaponModifiedEvent event) {
		for (WeaponModifiedListener listener : weaponModifiedListeners) {
			listener.onWeaponModified(event);
		}
	}
}

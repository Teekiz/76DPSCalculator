package Tekiz._DPSCalculator._DPSCalculator.services.events;

import org.springframework.context.ApplicationEvent;

public class WeaponChangedEvent extends ApplicationEvent
{
	public WeaponChangedEvent(Object source)
	{
		super(source);
	}
}

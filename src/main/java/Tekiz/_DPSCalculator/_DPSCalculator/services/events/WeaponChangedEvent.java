package Tekiz._DPSCalculator._DPSCalculator.services.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WeaponChangedEvent extends ApplicationEvent
{
	private final String description;
	public WeaponChangedEvent(Object source, String description)
	{
		super(source);
		this.description = description;
	}
}

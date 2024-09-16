package Tekiz._DPSCalculator._DPSCalculator.services.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConditionChangedEvent extends ApplicationEvent
{
	/*
		This event is used when a perk is removed or the condition is no longer applicable.
	 */

	//todo - possibly add a boolean for update
	private final String objectName;
	public ConditionChangedEvent(Object source, String objectName)
	{
		super(source);
		this.objectName = objectName;
	}
}

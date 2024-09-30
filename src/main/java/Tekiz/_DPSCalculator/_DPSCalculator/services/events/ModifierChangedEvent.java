package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import org.springframework.context.ApplicationEvent;

/**
 * An event which is called when a change to a {@link Modifier} within a {@link Loadout} has been made.
 */
public class ModifierChangedEvent extends ApplicationEvent
{
	private final String message;
	public ModifierChangedEvent(Object source, String message)
	{
		super(source);
		this.message = message;
	}
}

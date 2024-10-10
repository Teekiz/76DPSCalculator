package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * An event which is called when a change to a {@link Modifier} within a {@link Loadout} has been made.
 */
@Getter
public class ModifierChangedEvent extends ApplicationEvent
{
	private final Loadout loadout;
	private final String message;
	public ModifierChangedEvent(Object source, Loadout loadout, String message)
	{
		super(source);
		this.loadout = loadout;
		this.message = message;
	}
}

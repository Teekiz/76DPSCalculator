package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * An event which is called when a change to a {@code loadout}'s {@link EquippedArmour} special stat has been made.
 */
@Getter
public class ArmourChangedEvent extends ApplicationEvent
{
	private final Loadout loadout;
	private final String description;
	public ArmourChangedEvent(Object source, Loadout loadout, String description)
	{
		super(source);
		this.loadout = loadout;
		this.description = description;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * An event which is called when a change to a {@code loadout}'s {@link Player} special stats has been made.
 */
@Getter
public class SpecialsChangedEvent extends ApplicationEvent
{
	private final Loadout loadout;

	public SpecialsChangedEvent(Object source, Loadout loadout)
	{
		super(source);
		this.loadout = loadout;
	}
}
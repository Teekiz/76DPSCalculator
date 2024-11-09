package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.*;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * An event which is called when a change to a {@code loadout}'s {@link Player} special stat has been made.
 */
@Getter
public class SpecialChangedEvent extends ApplicationEvent
{
	private final Specials special;
	private final int availablePoints;
	private final Loadout loadout;

	public SpecialChangedEvent(Object source, Specials special, int availablePoints, Loadout loadout)
	{
		super(source);
		this.special = special;
		this.availablePoints = availablePoints;
		this.loadout = loadout;
	}
}

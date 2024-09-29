package Tekiz._DPSCalculator._DPSCalculator.services.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;

/**
 * An event which is called when a change to a {@code loadout}'s {@link Weapon} has been made.
 */
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

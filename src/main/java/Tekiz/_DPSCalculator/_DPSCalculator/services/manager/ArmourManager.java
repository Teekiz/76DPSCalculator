package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ArmourLoaderService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Armour} objects.
 */
@Service
@Getter
public class ArmourManager
{
	private final ArmourLoaderService armourLoaderService;

	/**
	 * The constructor for a {@link ArmourManager} object.
	 * @param armourLoaderService A service used to load {@link Armour} objects.
	 */
	@Autowired
	public ArmourManager(ArmourLoaderService armourLoaderService)
	{
		this.armourLoaderService = armourLoaderService;
	}
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
}

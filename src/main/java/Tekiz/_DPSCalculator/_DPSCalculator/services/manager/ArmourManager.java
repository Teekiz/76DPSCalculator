package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ArmourLoaderService;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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
		this.armour = new HashSet<>();
		this.armourLoaderService = armourLoaderService;
	}
}

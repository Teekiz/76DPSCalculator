package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
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
	private final DataLoaderService dataLoaderService;

	/**
	 * The constructor for a {@link ArmourManager} object.
	 * @param dataLoaderService A service used to load {@link Armour} objects.
	 */
	@Autowired
	public ArmourManager(DataLoaderService dataLoaderService)
	{
		this.dataLoaderService = dataLoaderService;
	}
}

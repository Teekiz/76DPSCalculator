package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Environment} objects.
 */
@Service
@Getter
public class EnvironmentManager
{
	/**
	 * The constructor for a {@link EnvironmentManager} object.
	 */
	@Autowired
	public EnvironmentManager()
	{

	}
}

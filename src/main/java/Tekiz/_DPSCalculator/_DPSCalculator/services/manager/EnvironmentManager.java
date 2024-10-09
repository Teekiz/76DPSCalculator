package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Environment} objects.
 */
@Service
@Getter
public class EnvironmentManager
{
	private final Environment environment;

	/**
	 * The constructor for a {@link EnvironmentManager} object.
	 */
	@Autowired
	public EnvironmentManager()
	{
		this.environment = new Environment();
	}
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
}

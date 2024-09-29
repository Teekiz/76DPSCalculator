package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Environment} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class EnvironmentManager implements LoadoutScopeClearable
{
	private Environment environment;

	/**
	 * The constructor for a {@link EnvironmentManager} object.
	 */
	@Autowired
	public EnvironmentManager()
	{
		this.environment = new Environment();
	}

	/**
	 * A method used during the cleanup of this service.
	 */
	@PreDestroy
	public void clear()
	{
		this.environment = null;
	}
}

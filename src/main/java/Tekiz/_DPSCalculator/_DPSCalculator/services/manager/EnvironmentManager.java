package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class EnvironmentManager implements LoadoutScopeClearable
{
	private Environment environment;

	@Autowired
	public EnvironmentManager()
	{
		this.environment = new Environment();
	}

	@PreDestroy
	public void clear()
	{
		this.environment = null;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class EnvironmentManager
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

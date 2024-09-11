package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ArmourLoaderService;
import jakarta.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class ArmourManager
{
	private Set<Armour> armour;
	private final ArmourLoaderService armourLoaderService;

	@Autowired
	public ArmourManager(ArmourLoaderService armourLoaderService)
	{
		this.armour = new HashSet<>();
		this.armourLoaderService = armourLoaderService;
	}

	@PreDestroy
	public void clear()
	{
		if (this.armour != null)
		{
			this.armour.clear();
		}
		this.armour = null;
	}
}

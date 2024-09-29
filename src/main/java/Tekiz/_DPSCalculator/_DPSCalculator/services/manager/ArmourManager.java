package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ArmourLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Armour} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class ArmourManager implements LoadoutScopeClearable
{
	private Set<Armour> armour;
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

	/**
	 * A method used during the cleanup of this service.
	 */
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

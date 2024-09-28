package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnvironmentManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Represents a storage object so that each manager is grouped to the same loadout based on {@code LoadoutID}.
 */

@Data
@Service
@Scope(value = "loadout")
public class Loadout
{
	private final int LoadoutID;
	private final WeaponManager weaponManager;
	private final PerkManager perkManager;
	private final ConsumableManager consumableManager;
	private final ArmourManager armourManager;
	private final PlayerManager playerManager;
	private final EnvironmentManager environmentManager;
	private final MutationManager mutationManager;

	/**
	 *
	 * @param weaponManager A {@link WeaponManager} service for storing and controlling all weapon details, including modifications.
	 * @param perkManager A {@link PerkManager} service for storing and controlling perks.
	 * @param consumableManager A {@link ConsumableManager} service for storing and controlling consumables.
	 * @param armourManager A {@link ArmourManager} service for storing and controlling all armour details, including modifications.
	 * @param playerManager A {@link PlayerManager} service for storing and controlling all player details.
	 * @param environmentManager A {@link EnvironmentManager} service for storing and controlling all environmental details.
	 * @param mutationManager A {@link MutationManager} service for storing and controlling all mutations.
	 */
	@Autowired
	public Loadout(WeaponManager weaponManager, PerkManager perkManager, ConsumableManager consumableManager,
				   ArmourManager armourManager, PlayerManager playerManager, EnvironmentManager environmentManager, MutationManager mutationManager)
	{
		this.LoadoutID = LoadoutScope.loadoutIdStorage.get();
		this.weaponManager = weaponManager;
		this.perkManager = perkManager;
		this.consumableManager = consumableManager;
		this.armourManager = armourManager;
		this.playerManager = playerManager;
		this.environmentManager = environmentManager;
		this.mutationManager = mutationManager;
	}
}

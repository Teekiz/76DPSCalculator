package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnvironmentManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ModifierManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
	private final ModifierManager modifierManager;

	@Autowired
	public Loadout(WeaponManager weaponManager, PerkManager perkManager, ConsumableManager consumableManager,
				   ArmourManager armourManager, PlayerManager playerManager, EnvironmentManager environmentManager, ModifierManager modifierManager)
	{
		this.LoadoutID = LoadoutScope.loadoutIdStorage.get();
		this.weaponManager = weaponManager;
		this.perkManager = perkManager;
		this.consumableManager = consumableManager;
		this.armourManager = armourManager;
		this.playerManager = playerManager;
		this.environmentManager = environmentManager;
		this.modifierManager = modifierManager;

	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.logic.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifiers;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnvironmentManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Data
@Service
@Scope("prototype")
public class Loadout
{
	private final int loadoutNumber;
	private final Modifiers modifiers;

	private final WeaponManager weaponManager;
	private final PerkManager perkManager;
	private final ConsumableManager consumableManager;
	private final ArmourManager armourManager;
	private final PlayerManager playerManager;
	private final EnvironmentManager environmentManager;

	@Autowired
	public Loadout(int loadoutNumber, WeaponManager weaponManager, PerkManager perkManager, ConsumableManager consumableManager,
				   ArmourManager armourManager, PlayerManager playerManager, EnvironmentManager environmentManager)
	{
		this.loadoutNumber = loadoutNumber;
		this.weaponManager = weaponManager;
		this.perkManager = perkManager;
		this.consumableManager = consumableManager;
		this.armourManager = armourManager;
		this.playerManager = playerManager;
		this.environmentManager = environmentManager;

		this.modifiers = new Modifiers();
	}
}

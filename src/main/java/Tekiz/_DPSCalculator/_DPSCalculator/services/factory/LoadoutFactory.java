package Tekiz._DPSCalculator._DPSCalculator.services.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.EnvironmentManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ModifierManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoadoutFactory
{
	@Autowired
	private ApplicationContext context;

	public Loadout createLoadout(int loadoutNumber)
	{
		WeaponManager weaponManager = context.getBean(WeaponManager.class);
		PerkManager perkManager = context.getBean(PerkManager.class);
		ConsumableManager consumableManager = context.getBean(ConsumableManager.class);
		ArmourManager armourManager = context.getBean(ArmourManager.class);
		PlayerManager playerManager = context.getBean(PlayerManager.class);
		EnvironmentManager environmentManager = context.getBean(EnvironmentManager.class);
		ModifierManager modifierManager = context.getBean(ModifierManager.class);

		return new Loadout(loadoutNumber, weaponManager, perkManager, consumableManager,
			armourManager, playerManager, environmentManager, modifierManager);
	}
}

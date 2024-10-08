package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoadoutFactory
{
	public Loadout createNewLoadout()
	{
		log.debug("Creating new loadout.");
		return new Loadout(null, new HashMap<>(), new HashMap<>(),
			new HashSet<>(), new Player(), new Environment(), new HashSet<>());
	}
	public Loadout createNewLoadout(Weapon weapon, HashMap<Perk, Boolean> perks, HashMap<Consumable, Boolean> consumables,
									Set<Armour> armour, Player player, Environment environment, Set<Mutation> mutations)
	{
		log.debug("Creating new loadout.");
		return new Loadout(weapon, perks, consumables, armour, player, environment, mutations);
	}
}

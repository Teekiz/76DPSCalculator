package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class LoadoutFactory
{
	public Loadout createNewLoadout(int loadoutID)
	{
		log.debug("Creating new blank loadout.");
		return new Loadout(loadoutID,null, new HashMap<>(), new HashMap<>(),
			new EquippedArmour(), new Player(), new Environment(), new HashSet<>(), null);
	}
	public Loadout createNewLoadout(int loadoutID, Weapon weapon, HashMap<Perk, Boolean> perks, HashMap<Consumable, Boolean> consumables,
									EquippedArmour armour, Player player, Environment environment, Set<Mutation> mutations)
	{
		log.debug("Retrieved loadout.");
		return new Loadout(loadoutID, weapon, perks, consumables, armour, player, environment, mutations, null);
	}
}

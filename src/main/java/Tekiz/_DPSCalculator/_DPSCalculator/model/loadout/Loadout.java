package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * Represents a loadout.
 */
@Getter
@Service
public class Loadout implements Serializable
{
	//@Autowired
	private Weapon weapon;
	private final HashMap<Perk, Boolean> perks;
	private final HashMap<Consumable, Boolean> consumables;
	private final Set<Armour> armour;
	private final Player player;
	private final Environment environment;
	private final Set<Mutation> mutations;

	public Loadout(Weapon weapon, HashMap<Perk, Boolean> perks, HashMap<Consumable, Boolean> consumables,
				   Set<Armour> armour, Player player,
				   Environment environment, Set<Mutation> mutations)
	{
		this.weapon = weapon;
		this.perks = perks;
		this.consumables = consumables;
		this.armour = armour;
		this.player = player;
		this.environment = environment;
		this.mutations = mutations;
	}
}

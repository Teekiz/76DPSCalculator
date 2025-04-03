package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Represents a loadout.
 */
@Value
public class Loadout implements Serializable
{
	@JsonProperty("loadoutID")
	int loadoutID;

	@NonFinal
	@Setter
	@JsonProperty("weapon")
	Weapon weapon;

	@JsonProperty("perks")
	HashMap<Perk, Boolean> perks;

	@JsonProperty("consumables")
	HashMap<Consumable, Boolean> consumables;

	@JsonProperty("armour")
	EquippedArmour armour;

	@JsonProperty("player")
	Player player;

	@JsonProperty("environment")
	Environment environment;

	@JsonProperty("mutations")
	Set<Mutation> mutations;

	@NonFinal
	@Setter
	@JsonProperty("enemy")
	Enemy enemy;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Loadout loadout = (Loadout) o;
		return loadoutID == loadout.loadoutID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(loadoutID);
	}
}

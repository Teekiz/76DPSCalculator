package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.LoadoutDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * Represents a loadout.
 */
@Value
@JsonDeserialize(using = LoadoutDeserializer.class)
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Loadout implements Serializable
{
	@NonFinal
	@Setter
	@JsonProperty("weapon")
	Weapon weapon;

	@JsonProperty("perks")
	HashMap<Perk, Boolean> perks;

	@JsonProperty("consumables")
	HashMap<Consumable, Boolean> consumables;

	@JsonProperty("armour")
	Set<Armour> armour;

	@JsonProperty("player")
	Player player;

	@JsonProperty("environment")
	Environment environment;

	@JsonProperty("mutations")
	Set<Mutation> mutations;
}

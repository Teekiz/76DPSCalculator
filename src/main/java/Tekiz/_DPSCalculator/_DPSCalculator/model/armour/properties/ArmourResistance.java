package Tekiz._DPSCalculator._DPSCalculator.model.armour.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Represents the resistances a given armour piece provides.
 */
public record ArmourResistance(@JsonProperty("damageResistance") int damageResistance,
							   @JsonProperty("energyResistance") int energyResistance,
							   @JsonProperty("radiationResistance") int radiationResistance,
							   @JsonProperty("cryoResistance") int cryoResistance,
							   @JsonProperty("fireResistance") int fireResistance,
							   @JsonProperty("poisonResistance") int poisonResistance) implements Serializable
{}

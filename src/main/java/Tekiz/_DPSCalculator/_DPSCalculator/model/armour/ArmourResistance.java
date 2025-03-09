package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Value;

/** Represents the resistances a given armour piece provides. */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ArmourResistance implements Serializable
{
	@JsonProperty("damageResistance")
	int damageResistance;
	@JsonProperty("energyResistance")
	int energyResistance;
	@JsonProperty("radiationResistance")
	int radiationResistance;
	@JsonProperty("cryoResistance")
	int cryoResistance;
	@JsonProperty("fireResistance")
	int fireResistance;
	@JsonProperty("poisonResistance")
	int poisonResistance;
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

/**
 * Represents a generic modification that can be made to a piece of armour. Each armour contains a {@code modName}
 * and a {@code armourSlot}. The armour piece is the body part a mod can be applied to.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class ArmourMod implements Serializable
{
	@Id
	@JsonProperty("id") @JsonAlias("_id")
	private final String id;
	@JsonProperty("name")
	private final String name;
	@JsonProperty("armourSlot")
	private final ArmourSlot armourSlot;
}

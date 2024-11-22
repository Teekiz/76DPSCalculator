package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Represents a generic modification that can be made to a piece of armour. Each armour contains a {@code modName}
 * and a {@code armourPiece}. The armour piece is the body part a mod can be applied to.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class ArmourMod implements Serializable
{
	@JsonProperty("id")
	private final String modId;
	@JsonProperty("modName")
	private final String modName;
	@JsonProperty("armourPiece")
	private final ArmourPiece armourPiece;
}

package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.LegendaryEffectDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * An object that represents the legendary effect slot.
 */
@Getter
@AllArgsConstructor
public class LegendaryEffectSlot
{
	/** The legendaryEffect that this slot currently has. */
	@DBRef
	@JsonDeserialize(using = LegendaryEffectDeserializer.class)
	@JsonProperty("currentLegendaryEffect") @JsonAlias("defaultLegendaryEffect")
	private LegendaryEffect currentLegendaryEffect;
	/** Whether the slot can be modified after creation. */
	private final boolean canSlotBeChanged;

	/**
	 * A method used to switch out the current legendary effect in this slot if the slot can be changed and is within the star type.
	 *
	 * @param newLegendaryEffect The new legendaryEffect to replace the current modification.
	 */
	public void changeCurrentLegendaryEffect(LegendaryEffect newLegendaryEffect){
		if (canSlotBeChanged){
			currentLegendaryEffect = newLegendaryEffect;
		}
	}
}

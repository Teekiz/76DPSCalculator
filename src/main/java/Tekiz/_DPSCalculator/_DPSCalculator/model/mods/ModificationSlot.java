package Tekiz._DPSCalculator._DPSCalculator.model.mods;

import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ModificationDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * An object that represents the modification slot of a weapon or armour piece.
 * @param <T> The type of modification this slot accepts.
 */
@Getter
@AllArgsConstructor
public class ModificationSlot<T extends Modification>
{
	/** The modification that this slot currently has. */
	@DBRef
	@JsonDeserialize(using = ModificationDeserializer.class)
	@JsonProperty("currentModification") @JsonAlias("defaultModification")
	private T currentModification;
	/** Whether the slot can be modified after creation. */
	private final boolean canSlotBeChanged;
	/** The alias names for the modifications available in this slot. */
	private final Set<String> availableInSlot;

	/**
	 * A method used to switch out the current modification in this slot if the slot can be changed and the alias is within the permitted list.
	 * @param newModification The new modification to replace the current modification.
	 */
	public void changeCurrentModification(T newModification){
		if (canSlotBeChanged && newModification != null && availableInSlot.contains(newModification.alias())){
			currentModification = newModification;
		}
	}
}

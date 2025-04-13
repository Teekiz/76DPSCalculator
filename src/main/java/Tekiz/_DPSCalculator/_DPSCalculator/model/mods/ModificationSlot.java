package Tekiz._DPSCalculator._DPSCalculator.model.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
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
	/**The type of mod this slot manages. */
	private ModType modType;
	/** Whether the slot can be modified after creation. */
	private final boolean canSlotBeChanged;
	/** The alias names for the modifications available in this slot. */
	private final Set<String> availableInSlot;

	/**
	 * A method used to switch out the current modification in this slot if the slot can be changed and the alias is within the permitted list.
	 * @param newModification The new modification to replace the current modification.
	 * @return {@code true} if the modification was successful. Returns {@code false} if the slot cannot be changed or the mod is an accepted mod type.
	 */
	public boolean changeCurrentModification(T newModification){
		if (canSlotBeChanged && newModification != null && newModification.modType().equals(newModification.modType())
			&& availableInSlot.contains(newModification.alias())){
			currentModification = newModification;
			return true;
		}
		return false;
	}
}

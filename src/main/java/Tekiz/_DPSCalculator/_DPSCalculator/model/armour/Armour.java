package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectObject;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents an armour piece that may be added to a user's loadout. */
@Getter
@SuperBuilder(toBuilder = true)
@Document(collection = "armour")
@RepositoryObject(repository = ArmourRepository.class)
public abstract class Armour implements LegendaryEffectObject, Serializable
{
	/** The id of the armour */
	@Id
	@JsonProperty("id") @JsonAlias("_id")
	protected final String id;

	/** The name of the armour. The user will be able to see the given value. */
	@JsonProperty("name")
	protected final String name;

	/** The level of the armour that the user will need to meet before it can be worn. The resistances provided by the armour are affected by its level. */
	@Setter
	@NonFinal
	@JsonProperty("armourLevel")
	protected int armourLevel;

	/** The type of the armour. Each armour type has specific limit to how many of each type can be worn. Armour or power armour consists of 6 pieces but only one type of
	 * under armour can be worn at any given time */
	@JsonProperty("armourType")
	protected final ArmourType armourType;

	/** The slot where the armour can be applied to.*/
	@JsonProperty("armourPiece")
	protected final ArmourPiece armourPiece;

	/** The slot the armour piece takes. This is dependent on the armour slot. */
	@NonFinal
	@JsonProperty("armourSlot")
	protected ArmourSlot armourSlot;

	/** The set the armour is a part of. If a set provides a bonus, the set effects matching the armour set name will be used. */
	@JsonProperty("armourSet")
	protected final String armourSet;

	/** A {@link HashMap} containing the level ({@link Integer}) and resistances ({@link ArmourResistance}) that the armour provides. */
	@JsonProperty("armourResistance")
	protected final HashMap<Integer, ArmourResistance> armourResistance;

	/** An object containing legendary effects in a HashMap*/
	@JsonProperty("legendaryEffects")
	protected final LegendaryEffectsMap legendaryEffects;

	/** An object to simplify modification lookups. */
	@JsonProperty("modifications")
	protected HashMap<ModType, ModificationSlot<ArmourMod>> modifications;

	/**
	 * A method that sets the armour slot if valid.
	 * @param newArmourSlot The slot the armour will take up.
	 */
	public void setArmourSlot(ArmourSlot newArmourSlot){
		if (newArmourSlot.getArmourPiece().equals(armourPiece)){
			armourSlot = newArmourSlot;
		}
	}

	/**
	 * A method that is used to make modifications to the armour.
	 * @param armourMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ModType}.
	 */
	@JsonIgnore
	public void setMod(ArmourMod armourMod)
	{
		if (armourMod == null){
			return;
		}

		ModificationSlot<ArmourMod> slot = modifications.get(armourMod.modType());
		if (slot != null){
			slot.changeCurrentModification(armourMod);
		}
	}

	/**
	 * A method that gets the effects from the modifications and legendary effects.
	 * @return A {@link List} of {@link Modifier}'s and {@link LegendaryEffect}'s.
	 */
	@JsonIgnore
	public List<Modifier> getAllModificationEffects()
	{
		List<Modifier> modifiers = new ArrayList<>(legendaryEffects != null ? legendaryEffects.getAllEffects() : List.of());

		if (modifications != null){
			modifiers.addAll(modifications.values().stream()
				.map(ModificationSlot::getCurrentModification)
				.filter(Objects::nonNull)
				.toList());
		}

		return modifiers;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectObject;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.HashMap;
import lombok.Getter;
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
	@NonFinal
	@JsonProperty("armourLevel")
	protected int armourLevel;

	/** The type of the armour. Each armour type has specific limit to how many of each type can be worn. Armour or power armour consists of 6 pieces but only one type of
	 * under armour can be worn at any given time */
	@JsonProperty("armourType")
	protected final ArmourType armourType;

	/** The slot the armour piece takes. This is dependent on the armour type. */
	@JsonProperty("armourPiece")
	protected final ArmourPiece armourPiece;

	/** The set the armour is a part of. If a set provides a bonus, this will determine it. */
	@JsonProperty("armourSet")
	protected final ArmourSet armourSet;

	/** A {@link HashMap} containing the level ({@link Integer}) and resistances ({@link ArmourResistance}) that the armour provides. */
	@JsonProperty("armourResistance")
	protected final HashMap<Integer, ArmourResistance> armourResistance;

	/** An object containing legendary effects in a HashMap*/
	@JsonProperty("legendaryEffects")
	protected final LegendaryEffectsMap legendaryEffects;

	@JsonIgnore
	public abstract void setMod(ArmourMod armourMod);
}

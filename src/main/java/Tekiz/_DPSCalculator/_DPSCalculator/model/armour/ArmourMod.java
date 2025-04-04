package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.Modification;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourModRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionAdapter;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ModifiersAdapter;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.expression.Expression;

/**
 * Represents a material modification that can be made to a piece of armour.
 * Each {@link ArmourMod} contains the bonus resistances and effect the mod may have.
 */

@Document(collection = "armour_modification")
@RepositoryObject(repository = ArmourModRepository.class)
public record ArmourMod(@Id
					   @JsonProperty("id") @JsonAlias("_id") String id,
						@JsonProperty("name") String name,
						@JsonProperty("alias") String alias,
						@JsonProperty("modType") ModType modType,
						@JsonProperty("modSubType") ModSubType modSubType,
						@JsonProperty("armourPiece") ArmourPiece armourPiece,
						@JsonProperty("armourResistance") HashMap<Integer, ArmourResistance> materialResistanceBonus,
						@JsonProperty("materialEffect") String materialEffect,
						@JsonProperty("modifierSource") ModifierSource modifierSource,
						@ValueConverter(value = ExpressionAdapter.ExpressionConverter.class)
						@JsonProperty("conditionString") Expression condition,
						@JsonProperty("effects")
						@JsonSerialize(using = ModifiersAdapter.ModifiersSerializer.class)
						@JsonDeserialize(using = ModifiersAdapter.ModifiersDeserializer.class)
						@ValueConverter(value = ModifiersAdapter.ModifiersConverter.class)
						HashMap<ModifierTypes, ModifierValue<?>> effects) implements Modification
{}

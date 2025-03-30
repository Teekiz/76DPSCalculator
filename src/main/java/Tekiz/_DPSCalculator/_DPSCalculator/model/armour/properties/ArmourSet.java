package Tekiz._DPSCalculator._DPSCalculator.model.armour.properties;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ModifiersAdapter;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.HashMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;
import org.springframework.expression.Expression;

/** Represents the effects a given armour piece provides. */
//todo - write set effects for some armour types
public record ArmourSet(@Id
						@JsonProperty("id") @JsonAlias("_id") String id,
						@JsonProperty("name") String name,
						@JsonProperty("modifierSource") ModifierSource modifierSource,
						@JsonProperty("conditionString") Expression condition,
						@JsonProperty("effects")
						@JsonSerialize(using = ModifiersAdapter.ModifiersSerializer.class)
						@JsonDeserialize(using = ModifiersAdapter.ModifiersDeserializer.class)
						@ValueConverter(value = ModifiersAdapter.ModifiersConverter.class)
						HashMap<ModifierTypes, ModifierValue<?>> effects) implements Modifier, Serializable
{}

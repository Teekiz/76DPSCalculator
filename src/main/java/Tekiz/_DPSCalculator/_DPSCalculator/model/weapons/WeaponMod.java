package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.Modification;
import Tekiz._DPSCalculator._DPSCalculator.persistence.WeaponModRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import Tekiz._DPSCalculator._DPSCalculator.services.aggregation.ModifierBoostService;
import Tekiz._DPSCalculator._DPSCalculator.services.context.ModifierExpressionService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
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
 * Represents a receiver modification for a ranged weapon.
 * @param name 						The name of the receiver.
 * @param alias						The name used to distinguish the receiver by.
 * @param modType				    The type of mod slot this mod uses (effects mods that can be replaced by this type).
 * @param modSubType 				Any distinguishing mod types (does not affect mod replacement).
 * @param modifierSource            The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
 *			                        to apply a modification to the receiver effects if a corresponding effect is available.
 * @param condition                 The condition required to use the modification.
 *                                  {@link ExpressionAdapter.ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
 *                                  is used to check the condition. If a condition string is not included, the modification will always be used.
 * @param effects                   The effects of the modification. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
 *                                  If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of the modification. This will be used by the
 *                                  {@link ModifierExpressionService} to determine the appropriate value.
 */
@Document(collection = "weapon_modifications")
@RepositoryObject(repository = WeaponModRepository.class)
public record WeaponMod(@Id
					   	@JsonProperty("id") @JsonAlias("_id") String id,
						@JsonProperty("name") String name,
						@JsonProperty("alias") String alias,
						@JsonProperty("modType") ModType modType,
						@JsonProperty("modSubType") ModSubType modSubType,
						@JsonProperty("modifierSource") ModifierSource modifierSource,
						@ValueConverter(value = ExpressionAdapter.ExpressionConverter.class)
					   	@JsonProperty("conditionString") Expression condition,
						@JsonProperty("effects")
						@JsonSerialize(using = ModifiersAdapter.ModifiersSerializer.class)
						@JsonDeserialize(using = ModifiersAdapter.ModifiersDeserializer.class)
						@ValueConverter(value = ModifiersAdapter.ModifiersConverter.class)
						HashMap<ModifierTypes, ModifierValue<?>> effects) implements Modification
{}

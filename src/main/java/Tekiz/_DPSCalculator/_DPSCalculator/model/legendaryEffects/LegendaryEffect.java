package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Keyable;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.persistence.LegendaryEffectRepository;
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
import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ValueConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.expression.Expression;

/**
 * Represents a legendary effect modifier that adds various effects to a user's loadout.
 * Each legendary effect has a condition that must be met before any effects are applied.
 *
 * @param id             An identifier used if the object has been retrieved from a database.
 *           		     This is not required if object has been stored in a JSON file.
 * @param name           The name of the legendary effect. The user will be able to see the given value.
 * @param description	 The description of the effects this object provides.
 * @param categories	 The objects this effect can be applied to (i.e. ranged weapons only or armour and power armour).
 * @param starType 		 The star slot this effect is applied to.
 * @param modifierSource The source type of the modifier ({@link ModifierSource}). This is used by the {@link ModifierBoostService}
 *                       to apply a modification to the consumable effects if a corresponding effect is available.
 * @param condition      The condition required to use the consumable. If the condition is not met, the effects will not be applied.
 *                       {@link ExpressionAdapter.ExpressionDeserializer} will take the string value of the property "conditionString" and convert it into an expression. {@link ModifierConditionLogic}
 *                       is used to check the condition. If a condition string is not included, the consumable will always be used.
 * @param effects        The effects of the consumable. An effect consists of a {@link ModifierTypes} and a value ({@link Integer} or {@link Double}).
 *                       If an effect requires additional logic to determine the applied value, use "ADDITIONAL_CONTEXT_REQUIRED" alongside the name of mutation. This will be used by the
 *                       {@link ModifierExpressionService} to determine the appropriate value.
 */
@Document("legendaryEffect")
@RepositoryObject(repository = LegendaryEffectRepository.class)
public record LegendaryEffect(@Id
						 @JsonProperty("id") @JsonAlias("_id") String id,
						 @JsonProperty("name") String name,
						 @JsonProperty("description") String description,
						 @JsonProperty("modifierSource") ModifierSource modifierSource,
						 @JsonProperty("Categories") List<Category> categories,
						 @JsonProperty("StarType") StarType starType,
						 @ValueConverter(value = ExpressionAdapter.ExpressionConverter.class)
						 @JsonProperty("conditionString") Expression condition,
						 @JsonProperty("effects")
						 @JsonSerialize(using = ModifiersAdapter.ModifiersSerializer.class)
						 @JsonDeserialize(using = ModifiersAdapter.ModifiersDeserializer.class)
						 @ValueConverter(value = ModifiersAdapter.ModifiersConverter.class)
						 HashMap<ModifierTypes, ModifierValue<?>> effects) implements Modifier, Keyable
{
	@Override
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		if (object == null || getClass() != object.getClass())
		{
			return false;
		}
		LegendaryEffect that = (LegendaryEffect) object;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, name);
	}
}

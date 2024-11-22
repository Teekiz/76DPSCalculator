package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonParser} to convert a {@link Map} of modifiers into an {@link Map} of {@link ModifierTypes} and {@link ModifierValue}.
 */
@Slf4j
public class ModifiersDeserializer extends JsonDeserializer<Map<ModifierTypes, ModifierValue<?>>>
{
	/**
	 * A method used to convert the name of a receiver into an {@link Expression} object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @return A {@link Map} of {@link ModifierTypes} and {@link ModifierValue} based on the values provided.
	 * @throws IOException If there is an error during the parsing process or reading input.
	 */
	@Override
	public Map<ModifierTypes, ModifierValue<?>> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException
	{

		ObjectNode node = jsonParser.getCodec().readTree(jsonParser);
		Map<ModifierTypes, ModifierValue<?>> result = new HashMap<>();

		log.debug("Received node: {}", node);

		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			ModifierTypes modifierType = ModifierTypes.valueOf(field.getKey());
			JsonNode valueNode = field.getValue();

			ModifierValue<?> modifierValue = null;
			if (valueNode.isInt()) {
				modifierValue = new ModifierValue<>(modifierType, valueNode.asInt());
			} else if (valueNode.isTextual()) {
				modifierValue = new ModifierValue<>(modifierType, valueNode.asText());
			} else if (valueNode.isDouble()) {
				modifierValue = new ModifierValue<>(modifierType, valueNode.asDouble());
			}

			result.put(modifierType, modifierValue);
		}
		return result;
	}
}

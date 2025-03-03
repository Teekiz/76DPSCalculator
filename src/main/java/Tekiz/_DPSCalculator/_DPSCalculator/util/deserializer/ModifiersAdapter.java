package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.PropertyValueConverter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.ValueConversionContext;
import org.springframework.data.convert.WritingConverter;
import org.bson.Document;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonParser} to convert a {@link Map} of modifiers into an {@link Map} of {@link ModifierTypes} and {@link ModifierValue}.
 */
@Slf4j
@JsonComponent
public class ModifiersAdapter
{
	public static class ModifiersSerializer extends JsonSerializer<Map<ModifierTypes, ModifierValue<?>>>
	{
		@Override
		public void serialize(Map<ModifierTypes, ModifierValue<?>> modifierTypesModifierValueMap, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
		{
			jsonGenerator.writeStartObject();

			for (Map.Entry<ModifierTypes, ModifierValue<?>> entry : modifierTypesModifierValueMap.entrySet()) {
				jsonGenerator.writeFieldName(entry.getKey().name());
				ModifierValue<?> modifierValue = entry.getValue();

				if (modifierValue.getValue() instanceof Integer) {
					jsonGenerator.writeNumber((Integer) modifierValue.getValue());
				} else if (modifierValue.getValue() instanceof Double) {
					jsonGenerator.writeNumber((Double) modifierValue.getValue());
				} else  {
					jsonGenerator.writeString((String) modifierValue.getValue());
				}
			}
			jsonGenerator.writeEndObject();
		}
	}

	public static class ModifiersDeserializer extends JsonDeserializer<Map<ModifierTypes, ModifierValue<?>>>
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

	public static class ModifiersConverter implements PropertyValueConverter<Map<ModifierTypes, ModifierValue<?>>, Document,ValueConversionContext<?>> {

		@Override
		public Map<ModifierTypes, ModifierValue<?>> read(Document source, @NotNull ValueConversionContext<?> context) {
			Map<ModifierTypes, ModifierValue<?>> result = new HashMap<>();

			for (String key : source.keySet()) {
				//convert to enum
				ModifierTypes modifierType = ModifierTypes.valueOf(key);

				//get value with key
				Object value = source.get(key);

				//conversion
				ModifierValue<?> modifierValue = null;
				if (value instanceof Integer) {
					modifierValue = new ModifierValue<>(modifierType, (Integer) value);
				} else if (value instanceof Double) {
					modifierValue = new ModifierValue<>(modifierType, (Double) value);
				} else if (value instanceof String) {
					modifierValue = new ModifierValue<>(modifierType, (String) value);
				}

				result.put(modifierType, modifierValue);
			}

			return result;
		}

		@Override
		public Document write(Map<ModifierTypes, ModifierValue<?>> value, @NotNull ValueConversionContext<?> context) {
			Document document = new Document();

			for (Map.Entry<ModifierTypes, ModifierValue<?>> entry : value.entrySet()) {
				String modifierType = entry.getKey().name();
				ModifierValue<?> modifierValue = entry.getValue();
				Object valueValue = modifierValue.getValue();

				if (valueValue instanceof Integer) {
					document.append(modifierType, (Integer) valueValue);
				} else if (valueValue instanceof Double) {
					document.append(modifierType, (Double) valueValue);
				} else if (valueValue instanceof String) {
					document.append(modifierType, (String) valueValue);
				}
			}

			return document;  // Return the populated Document
		}
	}

	//this is used specifically for perks as it cannot identify the inner map
	public static class PerkModifiersConverter implements PropertyValueConverter<Map<Integer, Map<ModifierTypes, ModifierValue<?>>>, Document,ValueConversionContext<?>>
	{
		private final ModifiersConverter modifiersConverter = new ModifiersConverter();

		@Override
		public Map<Integer, Map<ModifierTypes, ModifierValue<?>>> read(Document source, ValueConversionContext<?> context)
		{
			Map<Integer, Map<ModifierTypes, ModifierValue<?>>> result = new HashMap<>();

			for (String key : source.keySet()) {
				Integer rank = Integer.valueOf(key);

				Object nestedMap = source.get(key);
				if (nestedMap instanceof Document nestedDocument){
					Map<ModifierTypes, ModifierValue<?>> modifiersMap = modifiersConverter.read(nestedDocument, context);
					result.put(rank, modifiersMap);
				}
			}

			return result;
		}

		@Override
		public Document write(Map<Integer, Map<ModifierTypes, ModifierValue<?>>> value, ValueConversionContext<?> context)
		{
			Document document = new Document();
			for (Map.Entry<Integer, Map<ModifierTypes, ModifierValue<?>>> entry : value.entrySet()) {
				String rankKey = entry.getKey().toString();
				Map<ModifierTypes, ModifierValue<?>> modifiersMap = entry.getValue();

				// reuse existing converter for inner map
				Document nestedDocument = modifiersConverter.write(modifiersMap, context);
				document.append(rankKey, nestedDocument);
			}
			return document;
		}
	}

}


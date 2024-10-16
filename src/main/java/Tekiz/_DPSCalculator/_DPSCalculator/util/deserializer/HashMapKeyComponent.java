package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ConsumableFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.MutationFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.PerkFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.HashMap;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * A utility service to serialize and deserialize key objects in a {@link HashMap}.
 */
@Slf4j
public class HashMapKeyComponent
{
	public static class HashMapKeySerializer extends JsonSerializer<Keyable>
	{
		/**
		 * A method used to serialize a concrete {@link Keyable} object into a JSON string.
		 * Strings will contain either the {@link String} name or {@link Integer} ID when serializing and will be stored as:
		 * CLASSNAME_OBJECTNAME.
		 * @param keyable The object to be serialized.
		 * @param jsonGenerator An object used to convert the resulted string into Json.
		 * @param serializerProvider The serializerProvider (not used in this implementation).
		 * @throws IOException If expression cannot be converted or the {@link JsonGenerator} cannot write the string to Json.
		 */
		@Override
		public void serialize(Keyable keyable, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
		{
			log.debug("Received keyable: {}.", keyable);
			StringBuilder identifier = new StringBuilder("_");
			if (keyable.id() == 0) {
				 identifier.append(keyable.name().replaceAll(" ", "").toUpperCase());
			} else {
				identifier.append(keyable.id());
			}

			switch (keyable.getClass().getSimpleName())
			{
				case "Perk" -> identifier.insert(0, "PERK");
				case "Consumable" -> identifier.insert(0, "CONSUMABLE");
				case "Mutation" -> identifier.insert(0, "MUTATION");
				default -> identifier.insert(0, "UNKNOWN");
			}
			log.debug("Serialised key: {}.", identifier);
			jsonGenerator.writeFieldName(identifier.toString());
		}
	}
	public static class HashMapKeyDeserializer extends KeyDeserializer
	{
		/**
		 * A method to deserialize an object that is a key in a {@link HashMap}.
		 * @param string The string representation of the object formatted as CLASSNAME_OBJECTNAME_EXTRAINFO
		 * @param context The context for deserialization. Used for dependency injection.
		 * @return An object based on the {@code sting} data.
		 * @throws IOException If an object cannot be created.
		 */
		@Override
		public Object deserializeKey(String string, DeserializationContext context) throws IOException
		{
			String[] splitString = string.split("_");
			String objectType = splitString[0];
			String objectName = splitString[1];
			log.debug("Received string: {}. ObjectType: {}. ObjectName: {}", string, objectType, objectName);

			switch (objectType)
			{
				case "Perk" ->
				{
					log.debug("Deserializing Perk KeyObject: {}.", objectName);
					PerkFactory perkFactory = (PerkFactory) context.findInjectableValue(PerkFactory.class.getName(), null, null);
					return perkFactory.createPerk(objectName);
				}
				case "Consumable" ->
				{
					log.debug("Deserializing Consumable KeyObject: {}.", string);
					ConsumableFactory consumableFactory = (ConsumableFactory) context.findInjectableValue(ConsumableFactory.class.getName(), null, null);
					return consumableFactory.createConsumable(objectName);
				}
				case "Mutation" ->
				{
					log.debug("Deserializing Mutation KeyObject: {}.", string);
					MutationFactory mutationFactory = (MutationFactory) context.findInjectableValue(MutationFactory.class.getName(), null, null);
					return mutationFactory.createMutation(objectName);
				}
				case null, default ->
				{
					log.error("Could not deserialize key of type: {}.", objectType);
					return null;
				}
			}
		}
	}
}

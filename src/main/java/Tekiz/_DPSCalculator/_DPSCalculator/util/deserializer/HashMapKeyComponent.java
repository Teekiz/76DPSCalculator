package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Keyable;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.util.Arrays;
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
			if (keyable.id().equals("0")) {
				 identifier.append(keyable.name().replaceAll(" ", "").toUpperCase());
			} else {
				identifier.append(keyable.id());
			}

			switch (keyable.getClass().getSimpleName())
			{
				//A PERK object requires additional information to set the rank, so it will look like PERK_NAME_2 is set to 2.
				case "Perk" -> {
					Perk perk = (Perk) keyable;
					identifier.insert(0, "PERK");
					identifier.append("_").append(perk.perkRank().getCurrentRank());
				}
				case "Consumable" -> identifier.insert(0, "CONSUMABLE");
				case "Mutation" -> identifier.insert(0, "MUTATION");
				case "LegendaryEffect" -> identifier.insert(0, "LEGENDARYEFFECT");
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
			String objectIdentifier = splitString[1];
			String objectProperties[] = Arrays.copyOfRange(splitString, 2, splitString.length);

			log.debug("Received string: {}. ObjectType: {}. ObjectName: {}.", string, objectType, objectIdentifier);

			DataLoaderService loaderService = (DataLoaderService) context.
				findInjectableValue(DataLoaderService.class.getName(), null, null);

			switch (objectType.toUpperCase())
			{
				case "PERK" ->
				{
					log.debug("Deserializing Perk KeyObject: {}.", objectIdentifier);
					int rank = objectProperties.length >= 1 ? Integer.parseInt(objectProperties[0]) : 1;
					Perk perk = loaderService.loadData(objectIdentifier, Perk.class, null);
					perk.perkRank().setCurrentRank(rank);
					return perk;
				}
				case "CONSUMABLE" ->
				{
					log.debug("Deserializing Consumable KeyObject: {}.", string);
					return loaderService.loadData(objectIdentifier, Consumable.class, null);
				}
				case "MUTATION" ->
				{
					log.debug("Deserializing Mutation KeyObject: {}.", string);
					return loaderService.loadData(objectIdentifier, Mutation.class, null);
				}
				case "LEGENDARYEFFECT" ->
				{
					log.debug("Deserializing Legendary Effect KeyObject: {}.", string);
					return loaderService.loadData(objectIdentifier, LegendaryEffect.class, null);
				}
				case null, default ->
				{
					log.error("Could not deserialize key of type: {}. Name: {}.", objectType, objectIdentifier);
					return null;
				}
			}
		}
	}
}

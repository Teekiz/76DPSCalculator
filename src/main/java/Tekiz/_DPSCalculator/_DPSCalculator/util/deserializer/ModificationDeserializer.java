package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modification;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonParser} to convert the name of receiver into an {@link Receiver} object.
 */
@Slf4j
@JsonComponent
public class ModificationDeserializer extends JsonDeserializer<Modification>
{
	/**
	 * A method used to convert the name of a modification into an object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @param context The context for deserialization (not used in this implementation).
	 * @return An {@link Modification} object based on the name provided.
	 * @throws IOException If there is an error during the parsing process or reading input.
	 */
	@Override
	public Modification deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
	{
		JsonNode modificationNode = jsonParser.getCodec().readTree(jsonParser);
		Class<?> objectClass = getClassFromString(jsonParser.getParsingContext().getCurrentName());

		if (modificationNode == null)
		{
			log.error("Cannot deserialize modification: node is null.");
			return null;
		} else if (objectClass == null){
			log.error("Cannot deserialize modification: class cannot be determined.");
			return null;
		}

		try
		{
			DataLoaderService loaderService = (DataLoaderService) context.findInjectableValue(DataLoaderService.class.getName(), null, null);

			if (modificationNode.isTextual())
			{
				String ModificationIdentifier = modificationNode.asText();

				log.debug("Deserializing modification: '{}'", ModificationIdentifier);
				Modification modification = (Modification) loaderService.loadData(ModificationIdentifier, objectClass, null);
				//if the identifier is the name, not an ID, then try to load it using the file name
				if (modification == null) {
					modification = (Modification) loaderService.loadDataByName(ModificationIdentifier, objectClass, null);
				}
				return modification;
			}
			else if (modificationNode.isObject())
			{
				//needs to be retrieved from a database
				if (!modificationNode.get("collectionName").isNull()){
					String id = modificationNode.get("id").asText();
					return (Modification) loaderService.loadData(id, objectClass, null);
				} else {
					log.debug("Deserializing modification and creating object: '{}'", modificationNode.get("name").asText());
					return (Modification) jsonParser.getCodec().treeToValue(modificationNode, objectClass);
				}

			}
			else
			{
				log.error("Cannot deserialize modification: unrecognised type received.");
				return null;
			}
		} catch (Exception e)
		{
			log.error("Cannot deserialize modification as node: {}. {}", modificationNode, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * A method to determine the class to be constructed base on the node name.
	 * @param className The name of the class to be constructed.
	 * @return The {@link Class} that matches {@code className}. Otherwise, returns null.
	 */
	private Class<?> getClassFromString(String className){
		switch (className){
			case "receiver" -> 		{return Receiver.class;}
			case "material" -> 		{return Material.class;}
			case "miscellaneous" -> {return Miscellaneous.class;}
			default -> 				{return null;}
		}
	}
}

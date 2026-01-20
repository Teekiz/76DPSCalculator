package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

/**
 * A utility service used to allow a {@link JsonParser} to convert the name of a legendary effect into an {@link LegendaryEffect} object.
 */
@Slf4j
@JsonComponent
public class LegendaryEffectDeserializer extends JsonDeserializer<LegendaryEffect>
{
	/**
	 * A method used to convert the name of a legendary effect into an object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @param context The context for deserialization (not used in this implementation).
	 * @return An {@link LegendaryEffect} object based on the name provided.
	 * @throws IOException If there is an error during the parsing process or reading input.
	 */
	@Override
	public LegendaryEffect deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
	{
		ObjectCodec codec = jsonParser.getCodec();
		JsonNode effectNode = codec.readTree(jsonParser);

		if (effectNode.isMissingNode() || effectNode.isNull())
		{
			log.error("Cannot deserialize legendary effect: node is null.");
			return null;
		}

		try
		{
			DataLoaderService loaderService = (DataLoaderService) context.findInjectableValue(DataLoaderService.class.getName(), null, null);

			if (effectNode.isTextual())
			{
				String LegendaryEffectIdentifier = effectNode.asText();

				log.debug("Deserializing legendaryEffect: '{}'", LegendaryEffectIdentifier);
				LegendaryEffect legendaryEffect = loaderService.loadData(LegendaryEffectIdentifier, LegendaryEffect.class, null);
				//if the identifier is the name, not an ID, then try to load it using the file name
				if (legendaryEffect == null) {
					legendaryEffect = loaderService.loadDataByName(LegendaryEffectIdentifier, LegendaryEffect.class, null);
				}
				return legendaryEffect;
			}
			else if (effectNode.isObject())
			{
				//needs to be retrieved from a database
				JsonNode collection = effectNode.get("collectionName");
				if (collection != null && !collection.isNull()){
					String id = effectNode.get("id").asText();
					return loaderService.loadData(id, LegendaryEffect.class, null);
				} else {
					log.debug("Deserializing legendary effect and creating object: '{}'", effectNode.get("name").asText());
					return jsonParser.getCodec().treeToValue(effectNode, LegendaryEffect.class);
				}

			}
			else
			{
				log.error("Cannot deserialize modification: unrecognised type received.");
				return null;
			}
		} catch (Exception e)
		{
			log.error("Cannot deserialize modification as node: {}. {}", effectNode, e.getMessage(), e);
			return null;
		}
	}
}

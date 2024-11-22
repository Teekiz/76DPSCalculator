package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
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
public class ReceiverDeserializer extends JsonDeserializer<Receiver>
{
	/**
	 * A method used to convert the name of a receiver into an {@link Expression} object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @param context The context for deserialization (not used in this implementation).
	 * @return An {@link Receiver} object based on the name provided.
	 * @throws IOException If there is an error during the parsing process or reading input.
	 */
	@Override
	public Receiver deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
	{
		JsonNode receiverNode = jsonParser.getCodec().readTree(jsonParser);

		if (receiverNode == null)
		{
			return null;
		}

		try
		{
			if (receiverNode.isTextual())
			{
				String receiverIdentifier = receiverNode.asText();
				log.debug("Deserializing receiver: '{}'", receiverIdentifier);
				DataLoaderService loaderService = (DataLoaderService) context.findInjectableValue(DataLoaderService.class.getName(), null, null);
				return loaderService.loadData(receiverIdentifier, Receiver.class, null);
			}
			else if (receiverNode.isObject())
			{
				log.debug("Deserializing receiver and creating object: '{}'", receiverNode.get("name").asText());
				return jsonParser.getCodec().treeToValue(receiverNode, Receiver.class);
			}
			else
			{
				log.error("Cannot deserialize receiver: unrecognised type received.");
				return null;
			}
		} catch (Exception e)
		{
			log.error("Cannot deserialize receiver as node: {}. {}", receiverNode, e.getMessage(), e);
			return null;
		}
	}
}

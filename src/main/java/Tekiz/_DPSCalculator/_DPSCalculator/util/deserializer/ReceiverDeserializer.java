package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ModLoaderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonParser} to convert the name of receiver into an {@link Receiver} object.
 */
@Slf4j
public class ReceiverDeserializer extends JsonDeserializer<Receiver>
{
	//todo - consider making this to mod deserializer
	private final ModLoaderService modLoaderService;
	private final ObjectMapper objectMapper;

	/**
	 * The constructor for a {@link ReceiverDeserializer} object.
	 * @param modLoaderService A service that converts a JSON object into a {@link Receiver} object.
	 */
	public ReceiverDeserializer(ModLoaderService modLoaderService)
	{
		this.modLoaderService = modLoaderService;
		this.objectMapper = new ObjectMapper();
	}

	 /**
	 * A method used to convert the name of a receiver into an {@link Expression} object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @param deserializationContext The context for deserialization (not used in this implementation).
	 * @return An {@link Receiver} object based on the name provided.
	 * @throws IOException If there is an error during the parsing process or reading input.
	 */
	@Override
	public Receiver deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
	{
		JsonNode receiverNode = jsonParser.getCodec().readTree(jsonParser);

		try
		{
			//todo - delete
			System.out.println(receiverNode);

			if (receiverNode.isTextual())
			{
				String receiverName = receiverNode.asText();
				log.debug("Deserializing receiver: '{}'", receiverName);
				return modLoaderService.getReceiver(receiverName);
			}
			else
			{
				log.debug("Deserializing receiver and creating object: '{}'", receiverNode.get("name").asText());
				return objectMapper.treeToValue(receiverNode, Receiver.class);
			}
		} catch (Exception e)
		{
			log.error("Cannot deserialize receiver as node: {}. {}", receiverNode, e.getMessage(), e);
			return null;
		}
	}
}

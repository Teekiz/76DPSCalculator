package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.expression.Expression;

/**
 * A utility service used to allow a {@link JsonParser} to convert the name of receiver into an {@link Receiver} object.
 */
public class ReceiverDeserializer extends JsonDeserializer<Receiver>
{
	//todo - consider making this to mod deserializer
	private final ModLoaderService modLoaderService;

	/**
	 * The constructor for a {@link ReceiverDeserializer} object.
	 * @param modLoaderService A service that converts a JSON object into a {@link Receiver} object.
	 */
	public ReceiverDeserializer(ModLoaderService modLoaderService)
	{
		this.modLoaderService = modLoaderService;
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
		String receiverName = jsonParser.getText();
		return modLoaderService.getReceiver(receiverName);
	}
}

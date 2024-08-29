package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.ModLoaderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class ReceiverDeserializer extends JsonDeserializer<Receiver>
{
	private final ModLoaderService modLoaderService;
	public ReceiverDeserializer(ModLoaderService modLoaderService)
	{
		this.modLoaderService = modLoaderService;
	}
	@Override
	public Receiver deserialize(JsonParser parser, DeserializationContext context) throws IOException
	{
		String receiverName = parser.getText();
		return modLoaderService.getReceiver(receiverName);
	}
}

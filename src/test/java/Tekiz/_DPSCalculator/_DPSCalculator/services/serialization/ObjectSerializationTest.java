package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ObjectSerializationTest
{
	@Autowired
	ConsumableLoaderService consumableLoaderService;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void serializeAndDeserializeConsumable() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeConsumable in ObjectSerializationTest.", System.lineSeparator());
		Consumable consumable = consumableLoaderService.getConsumable("TESTEVENT");
		assertNotNull(consumable);
		log.debug("Consumable object deserialized: {}.", consumable);

		String jsonConsumable = objectMapper.writeValueAsString(consumable);
		assertNotNull(jsonConsumable);
		log.debug("Consumable object serialized: {}.", jsonConsumable);

		Consumable newConsumable = objectMapper.readValue(jsonConsumable, Consumable.class);
		assertNotNull(newConsumable);
		log.debug("Consumable object deserialized: {}.", newConsumable);
	}
}

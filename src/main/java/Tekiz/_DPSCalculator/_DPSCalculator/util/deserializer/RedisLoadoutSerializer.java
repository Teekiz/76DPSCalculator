package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Slf4j
public class RedisLoadoutSerializer implements RedisSerializer<HashMap<Integer, Loadout>>
{
	private final ObjectMapper objectMapper;

	public RedisLoadoutSerializer(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public byte[] serialize(HashMap<Integer, Loadout> value)
	{
		log.debug("Serializing loadout: {}", value);

		if (value == null) {
			log.error("Cannot serialize loadout. Provided HashMap is null.");
			return new byte[0];
		}

		try {
			return objectMapper.writeValueAsBytes(value);
		} catch (JsonProcessingException | SerializationException e) {
			log.error("Cannot serialize loadout: {}. {}", value, e.getMessage(), e);
			return new byte[0];
		}
	}

	@Override
	public HashMap<Integer, Loadout> deserialize(byte[] bytes)
	{
		log.debug("Deserializing loadout: {}", bytes);

		if (bytes == null || bytes.length == 0)
		{
			log.error("Cannot deserialize loadout. Provided byte array is null or empty.");
			return null;
		}

		try {
			return objectMapper.readValue(bytes, objectMapper.getTypeFactory().constructMapType(HashMap.class, Integer.class, Loadout.class));
		} catch (IOException | SerializationException e) {
			log.error("Cannot deserialize loadout: {}. {}", bytes, e.getMessage(), e);
			return null;
		}
	}
}

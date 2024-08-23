package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ModLoader
{
	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<Receiver> loadReceivers(File file) throws IOException
	{
		return objectMapper.readValue(file, new TypeReference<List<Receiver>>() {});
	}

}

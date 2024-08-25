package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service @Getter
public class ModLoaderService
{
	//todo - change from static path
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final File file = new File("src/main/resources/data/receivers.json");
	private final List<Receiver> receivers;

	public ModLoaderService() throws IOException
	{
		receivers = loadReceivers();
	}

	public List<Receiver> loadReceivers() throws IOException
	{
		return objectMapper.readValue(file, new TypeReference<List<Receiver>>() {});
	}

}

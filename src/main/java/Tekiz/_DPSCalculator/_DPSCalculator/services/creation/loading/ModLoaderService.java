package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.util.loading.JSONLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ModLoaderService
{
	private final ObjectMapper objectMapper;
	private final File receiversFile;

	@Lazy
	@Autowired
	public ModLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.receiversFile = new File(fileConfig.getPaths().get("receiver"));
	}

	public Receiver getReceiver(String receiverName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(receiversFile, receiverName);
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		return objectMapper.treeToValue(rootNode, Receiver.class);
	}

	public List<Receiver> getAllReceivers() throws IOException
	{
		List<Receiver> receivers = new ArrayList<>();
		List<File> jsonFiles = JSONLoader.getAllJSONFiles(receiversFile);
		for (File file : jsonFiles)
		{
			JsonNode rootNode = objectMapper.readTree(file);
			if (rootNode != null)
			{
				receivers.add(objectMapper.treeToValue(rootNode, Receiver.class));
			}
		}
		return receivers;
	}

}

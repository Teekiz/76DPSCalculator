package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.util.loading.JSONLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumableLoaderService
{
	private final ObjectMapper objectMapper;
	private final File consumableFile;

	@Autowired
	public ConsumableLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.consumableFile = new File(fileConfig.getPaths().get("consumable"));
	}

	public Consumable getConsumable(String consumableName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(consumableFile, consumableName);
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		return objectMapper.treeToValue(rootNode, Consumable.class);
	}

	public List<Consumable> getAllConsumables() throws IOException
	{
		List<Consumable> consumables = new ArrayList<>();
		List<File> jsonFiles = JSONLoader.getAllJSONFiles(consumableFile);
		for (File file : jsonFiles)
		{
			JsonNode rootNode = objectMapper.readTree(file);
			if (rootNode != null)
			{
				consumables.add(objectMapper.treeToValue(rootNode, Consumable.class));
			}
		}
		return consumables;
	}

}

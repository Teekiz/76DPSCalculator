package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
		JsonNode rootNode = objectMapper.readTree(consumableFile);
		JsonNode consumableNode = rootNode.get(consumableName.toUpperCase());
		return objectMapper.treeToValue(consumableNode, Consumable.class);
	}

	public List<Consumable> getAllConsumables() throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(consumableFile);
		List<Consumable> consumables = new ArrayList<>();
		Iterator<JsonNode> elements = rootNode.elements();

		while (elements.hasNext()) {
			JsonNode consumableNode = elements.next();
			Consumable consumable = objectMapper.treeToValue(consumableNode, Consumable.class);
			consumables.add(consumable);
		}

		return consumables;
	}

}

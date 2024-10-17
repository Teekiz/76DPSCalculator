package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.util.loading.JSONLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArmourLoaderService
{
	private final ObjectMapper objectMapper;
	private final File armourFile;

	@Autowired
	public ArmourLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.armourFile = new File(fileConfig.getPaths().get("armour"));
	}

	public Armour getArmour(String armourName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(armourFile, armourName);
		if (jsonFile != null)
		{
			JsonNode rootNode = objectMapper.readTree(jsonFile);
			return objectMapper.treeToValue(rootNode, Armour.class);
		}
		else
		{
			log.error("Cannot deserialize {}. Armour file is null ({}).", armourName, armourFile);
			return null;
		}
	}

	public List<Armour> getAllArmourPieces() throws IOException
	{
		List<Armour> armour = new ArrayList<>();
		List<File> jsonFiles = JSONLoader.getAllJSONFiles(armourFile);
		for (File file : jsonFiles)
		{
			JsonNode rootNode = objectMapper.readTree(file);
			if (rootNode != null)
			{
				armour.add(objectMapper.treeToValue(rootNode, Armour.class));
			}
		}
		return armour;
	}
}

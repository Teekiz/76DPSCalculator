package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.util.loading.JSONLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ArmourModLoaderService
{
	private final ObjectMapper objectMapper;
	private final File armourModMaterialFile;
	private final File armourModMiscFile;

	@Autowired
	public ArmourModLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.armourModMaterialFile = new File(fileConfig.getPaths().get("armourMaterial"));
		this.armourModMiscFile = new File(fileConfig.getPaths().get("armourMisc"));
	}

	public Material getMaterial(String materialName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(armourModMaterialFile, materialName);
		if (jsonFile != null)
		{
			JsonNode rootNode = objectMapper.readTree(jsonFile);
			return objectMapper.treeToValue(rootNode, Material.class);
		}
		else
		{
			log.error("Cannot deserialize {}. Material file is null ({}).", materialName, armourModMaterialFile);
			return null;
		}
	}

	public Miscellaneous getMiscellaneous(String miscellaneousName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(armourModMiscFile, miscellaneousName);
		if (jsonFile != null)
		{
			JsonNode rootNode = objectMapper.readTree(jsonFile);
			return objectMapper.treeToValue(rootNode, Miscellaneous.class);
		}
		else
		{
			log.error("Cannot deserialize {}. Miscellaneous file is null ({}).", miscellaneousName, armourModMiscFile);
			return null;
		}
	}
}

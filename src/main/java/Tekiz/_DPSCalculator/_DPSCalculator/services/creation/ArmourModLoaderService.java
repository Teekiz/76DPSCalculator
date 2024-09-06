package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ArmourModLoaderService
{
	private final ObjectMapper objectMapper;
	private final File armourModMaterialFile;
	private final File armourModMiscFile;

	@Autowired
	public ArmourModLoaderService(ObjectMapper objectMapper, @Value("${armourMaterial.data.file.path}") String armourModMaterialFilePath,
								  @Value("${armourMisc.data.file.path}") String armourModMiscFilePath)
	{
		this.objectMapper = objectMapper;
		this.armourModMaterialFile = new File(armourModMaterialFilePath);
		this.armourModMiscFile = new File(armourModMiscFilePath);
	}

	public Material getMaterial(String materialName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(armourModMaterialFile);
		JsonNode materialNode = rootNode.get(materialName.toUpperCase());
		return objectMapper.treeToValue(materialNode, Material.class);
	}

	public Miscellaneous getMiscellaneous(String miscellaneousName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(armourModMiscFile);
		JsonNode miscellaneousNode = rootNode.get(miscellaneousName.toUpperCase());
		return objectMapper.treeToValue(miscellaneousNode, Miscellaneous.class);
	}
}

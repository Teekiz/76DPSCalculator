package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PerkLoaderService
{
	private final ObjectMapper objectMapper;
	private final File perkFile;

	@Autowired
	public PerkLoaderService(ObjectMapper objectMapper, @Value("${perk.data.file.path}") String perkDataFilePath) throws IOException
	{
		this.objectMapper = objectMapper;
		this.perkFile = new File(perkDataFilePath);
	}

	public Perk getPerk(String perkName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(perkFile);
		JsonNode perkNode = rootNode.get(perkName.toUpperCase());
		return objectMapper.treeToValue(perkNode, Perk.class);
	}
}

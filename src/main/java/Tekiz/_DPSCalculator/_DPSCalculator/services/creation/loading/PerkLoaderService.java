package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
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
public class PerkLoaderService
{
	private final ObjectMapper objectMapper;
	private final File perkFile;

	@Autowired
	public PerkLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.perkFile = new File(fileConfig.getPaths().get("perk"));
	}

	public Perk getPerk(String perkName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(perkFile, perkName);
		if (jsonFile != null)
		{
			JsonNode rootNode = objectMapper.readTree(jsonFile);
			return objectMapper.treeToValue(rootNode, Perk.class);
		}
		else
		{
			log.error("Cannot deserialize {}. Perk file is null ({}).", perkName, perkFile);
			return null;
		}

	}
}

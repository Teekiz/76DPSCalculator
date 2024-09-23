package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ArmourLoaderService
{
	private final ObjectMapper objectMapper;
	private final File armourFile;

	@Autowired
	public ArmourLoaderService(ObjectMapper objectMapper, FileConfig fileConfig)
	{
		this.objectMapper = objectMapper;
		this.armourFile = new File(fileConfig.getPaths().get("armour"));
	}

	public Armour getArmour(String armourName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(armourFile);
		JsonNode armourNode = rootNode.get(armourName.toUpperCase());
		return objectMapper.treeToValue(armourNode, Armour.class);
	}

	public List<Armour> getAllArmourPieces() throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(armourFile);
		List<Armour> armour = new ArrayList<>();
		Iterator<JsonNode> elements = rootNode.elements();

		while (elements.hasNext()) {
			JsonNode armourNode = elements.next();
			Armour armourPiece = objectMapper.treeToValue(armourNode, Armour.class);
			armour.add(armourPiece);
		}

		return armour;
	}
}

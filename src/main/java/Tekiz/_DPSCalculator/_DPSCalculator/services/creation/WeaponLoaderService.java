package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.WeaponFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeaponLoaderService
{
	private final WeaponFactory weaponFactory;
	private final ObjectMapper objectMapper;
	private final File weaponFile;

	@Autowired
	public WeaponLoaderService(WeaponFactory weaponFactory, ObjectMapper objectMapper, FileConfig fileConfig)
	{
		this.weaponFactory = weaponFactory;
		this.objectMapper = objectMapper;
		this.weaponFile = new File(fileConfig.getPaths().get("weapon"));
	}

	public Weapon getWeapon(String weaponName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(weaponFile);
		JsonNode weaponNode = rootNode.get(weaponName.toUpperCase());
		if (weaponNode != null)
		{
			return weaponFactory.createWeapon(weaponNode);
		}
		return null;
	}

	//This will be used to for weapon displays so that unnecessary objects are not created.
	public HashMap<String, String> getAllWeaponsWithName() throws IOException
	{
		HashMap<String, String> weaponNames = new HashMap<>();
		JsonNode rootNode = objectMapper.readTree(weaponFile);
		Iterator<String> fieldNames = rootNode.fieldNames();
		while (fieldNames.hasNext())
		{
			String rootFieldName = fieldNames.next();
			JsonNode childNode = rootNode.get(rootFieldName);
			String childNameValue = childNode.get("weaponName").asText();
			weaponNames.put(rootFieldName, childNameValue);
		}

		return weaponNames;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.Weapon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Getter
public class WeaponLoaderService
{
	//todo - change from static path
	private final WeaponFactory weaponFactory;
	private final ObjectMapper objectMapper;
	private final File file = new File("src/main/resources/data/weapons.json");

	//todo - consider changing to method
	//todo - pass object mapper?
	@Autowired
	public WeaponLoaderService(WeaponFactory weaponFactory) throws IOException
	{
		this.weaponFactory = weaponFactory;
		objectMapper = new ObjectMapper();
	}

	public List<String> loadWeaponNameList() throws IOException
	{
		List<String> weaponNames = new ArrayList<>();
		JsonNode rootNode = objectMapper.readTree(file);
		Iterator<String> names = rootNode.fieldNames();
		while (names.hasNext())
		{
			weaponNames.add(names.next());
		}

		return weaponNames;
	}

	public Weapon getWeapon(String weaponName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(file);
		JsonNode weaponNode = rootNode.get(weaponName.toUpperCase());
		if (weaponNode != null)
		{
			return weaponFactory.createWeapon(weaponNode);
		}
		return null;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
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
public class WeaponLoaderService
{
	private final WeaponFactory weaponFactory;
	private final ObjectMapper objectMapper;
	private final File weaponFile;

	@Autowired
	public WeaponLoaderService(WeaponFactory weaponFactory, ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.weaponFactory = weaponFactory;
		this.objectMapper = objectMapper;
		this.weaponFile = new File(fileConfig.getPaths().get("weapon"));
	}

	public Weapon getWeapon(String weaponName) throws IOException
	{
		//removes spaces and converts to uppercase
		String modifiedWeaponName = weaponName.replaceAll("\\s+", "").toUpperCase();
		File jsonFile = JSONLoader.getJSONFile(weaponFile, modifiedWeaponName);
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		if (rootNode != null)
		{
			return weaponFactory.createWeapon(rootNode);
		}
		return null;
	}

	//This will be used to for weapon displays so that unnecessary objects are not created.
	public List<Weapon> getAllWeapons() throws IOException
	{
		List<Weapon> weapons = new ArrayList<>();
		List<File> jsonFiles = JSONLoader.getAllJSONFiles(weaponFile);
		for (File file : jsonFiles)
		{
			JsonNode rootNode = objectMapper.readTree(file);
			if (rootNode != null)
			{
				weapons.add(weaponFactory.createWeapon(rootNode));
			}
		}
		return weapons;
	}
}

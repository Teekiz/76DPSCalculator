package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.Pistol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeaponFactory
{
	private final ObjectMapper objectMapper;

	@Autowired
	public WeaponFactory(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	public Weapon createWeapon(JsonNode weapon) throws JsonProcessingException
	{
		WeaponType weaponType = WeaponType.valueOf(weapon.get("weaponType").asText().toUpperCase());

		if (weaponType.equals(WeaponType.PISTOL))
		{
			return objectMapper.treeToValue(weapon, Pistol.class);
		}
		else if (weaponType.equals(WeaponType.RIFLE))
		{
			//return rifle
		}
		return null;
	}
}

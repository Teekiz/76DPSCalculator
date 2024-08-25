package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.Pistol;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class WeaponFactory
{
	private final ObjectMapper objectMapper;

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

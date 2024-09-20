package Tekiz._DPSCalculator._DPSCalculator.services.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.meleeweapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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

		if (weaponType.equals(WeaponType.PISTOL) || weaponType.equals(WeaponType.RIFLE))
		{
			return objectMapper.treeToValue(weapon, RangedWeapon.class);
		}
		else if (weaponType.equals(WeaponType.ONEHANDED) || weaponType.equals(WeaponType.TWOHANDED))
		{
			return objectMapper.treeToValue(weapon, MeleeWeapon.class);
		}
		return null;
	}
}

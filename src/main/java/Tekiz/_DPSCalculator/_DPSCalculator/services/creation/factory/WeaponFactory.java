package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A factory service for a creating {@link Weapon} objects based on their {@link WeaponType}.
 */
@Component
@Slf4j
public class WeaponFactory
{

	//todo - could introduce null object pattern. Create a weapon which has no stats.
	private final ObjectMapper objectMapper;

	/**
	 * The constructor for a {@link WeaponFactory} object.
	 * @param objectMapper The ObjectMapper instance used to parse JSON into weapon objects.
	 */
	@Autowired
	public WeaponFactory(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	/**
	 * Creates a {@link Weapon} object based on the provided JSON node.
	 * Depending on the {@code weaponType} field, the method returns a {@link RangedWeapon}
	 * or {@link MeleeWeapon}.
	 *
	 * @param weapon The JSON node containing the weapon data.
	 * @return A {@link Weapon} object, which could be either a {@link RangedWeapon} or {@link MeleeWeapon}, or {@code null} if the weapon type is not recognized.
	 */
	public Weapon createWeapon(JsonNode weapon)
	{
		try
		{
			JsonNode weaponTypeNode = weapon.get("weaponType");
			if (weaponTypeNode == null || !weaponTypeNode.isTextual()) {
				log.error("Cannot deserialize node. WeaponType is missing or null.");
				return null;
			}
			WeaponType weaponType = WeaponType.valueOf(weaponTypeNode.asText().toUpperCase());
			if (weaponType.equals(WeaponType.PISTOL) || weaponType.equals(WeaponType.RIFLE)) {
				return objectMapper.treeToValue(weapon, RangedWeapon.class);
			}
			else if (weaponType.equals(WeaponType.ONEHANDED) || weaponType.equals(WeaponType.TWOHANDED)) {
				return objectMapper.treeToValue(weapon, MeleeWeapon.class);
			}
			return null;
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			log.error("Cannot deserialise node. {}", e.getMessage(), e);
			return null;
		}
		catch (Exception e)
		{
			log.error("Failed to create weapon. {}", e.getMessage(), e);
			return null;
		}
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * A factory service for a creating {@link Weapon} objects based on their {@link WeaponType}.
 */
@Component
@Slf4j
public class WeaponFactory implements Factory<Weapon>
{

	//todo - could introduce null object pattern. Create a weapon which has no stats.
	//todo - consider switching around, so that the factory calls the loader
	private final ObjectMapper objectMapper;

	/**
	 * The constructor for a {@link WeaponFactory} object.
	 * @param objectMapper The ObjectMapper instance used to parse JSON into weapon objects.
	 */
	@Lazy
	@Autowired
	public WeaponFactory(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public Weapon createObject(Object weaponObject)
	{
		//if more loading methods are required, these can be added.
		if (weaponObject instanceof JsonNode) {
			return createFromJsonNode((JsonNode) weaponObject);
		}
		else if (weaponObject instanceof Document){
			return createFromDocument((Document) weaponObject);
		} else {
			log.error("Unrecognised object type. Cannot create object.");
			return null;
		}
	}

	/**
	 * Creates a {@link Weapon} object based on the provided JSON node.
	 * Depending on the {@code weaponType} field, the method returns a {@link RangedWeapon}
	 * or {@link MeleeWeapon}.
	 *
	 * @param weaponNode The JSON node containing the weaponNode data.
	 * @return A {@link Weapon} object, which could be either a {@link RangedWeapon} or {@link MeleeWeapon}, or {@code null} if the weaponNode type is not recognized.
	 */
	private Weapon createFromJsonNode(JsonNode weaponNode)
	{
		try
		{
			JsonNode weaponTypeNode = weaponNode.get("weaponType");
			if (weaponTypeNode == null || !weaponTypeNode.isTextual()) {
				//an error log is not thrown as this will be expected if the loadout has been deserialized with no set weaponNode.
				log.warn("Cannot deserialize node. WeaponType is missing or null.");
				return null;
			}

			WeaponType weaponType = WeaponType.valueOf(weaponTypeNode.asText().toUpperCase());
			if (weaponType.equals(WeaponType.PISTOL) || weaponType.equals(WeaponType.RIFLE)) {
				return objectMapper.treeToValue(weaponNode, RangedWeapon.class);
			}
			else if (weaponType.equals(WeaponType.ONEHANDED) || weaponType.equals(WeaponType.TWOHANDED)) {
				return objectMapper.treeToValue(weaponNode, MeleeWeapon.class);
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
			log.error("Failed to create weaponNode. {}", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * A method to convert a {@link Document} to a JSON node before passing it to {@code createFromJsonNode}.
	 * @param document The document to be converted.
	 * @return A {@link Weapon} object, which could be either a {@link RangedWeapon} or {@link MeleeWeapon}, or {@code null} if the weaponNode type is not recognized.
	 */
	private Weapon createFromDocument(Document document)
	{
		if (document == null)
		{
			return null;
		}

		JsonNode jsonNode = objectMapper.valueToTree(document);
		return createFromJsonNode(jsonNode);
	}
}

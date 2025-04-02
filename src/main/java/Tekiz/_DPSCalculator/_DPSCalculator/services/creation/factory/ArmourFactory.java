package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.UnderArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.OverArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.PowerArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * A factory service for a creating {@link Armour} objects based on their {@link ArmourType}.
 */
@Component
@Slf4j
public class ArmourFactory implements Factory<Armour>
{
	private final ObjectMapper objectMapper;

	/**
	 * The constructor for a {@link ArmourFactory} object.
	 * @param objectMapper The ObjectMapper instance used to parse JSON into weapon objects.
	 */
	@Lazy
	@Autowired
	public ArmourFactory(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
	}

	@Override
	public Armour createObject(Object armourObject)
	{
		//if more loading methods are required, these can be added.
		if (armourObject instanceof JsonNode) {
			return createFromJsonNode((JsonNode) armourObject);
		}
		else if (armourObject instanceof Document){
			return createFromDocument((Document) armourObject);
		} else {
			log.error("Unrecognised object type. Cannot create object.");
			return null;
		}
	}

	/**
	 * Creates a {@link Armour} object based on the provided JSON node.
	 * Depending on the {@code armourType} field, the method returns a {@link UnderArmour}, {@link OverArmourPiece} or {@link PowerArmourPiece}.
	 *
	 * @param armourNode The JSON node containing the weaponNode data.
	 * @return A {@link Armour} object, which could be either a {@link UnderArmour}, {@link OverArmourPiece} or {@link PowerArmourPiece}, or {@code null} if the armourNode type is not recognized.
	 */
	private Armour createFromJsonNode(JsonNode armourNode)
	{
		try
		{
			JsonNode armourTypeNode = armourNode.get("armourType");
			if (armourTypeNode == null || !armourTypeNode.isTextual()) {
				//an error log is not thrown as this will be expected if the loadout has been deserialized with no set weaponNode.
				log.warn("Cannot deserialize node. ArmourType is missing or null.");
				return null;
			}

			ArmourType weaponType = ArmourType.valueOf(armourTypeNode.asText().toUpperCase());
			if (weaponType.equals(ArmourType.UNDER_ARMOUR)) {
				return objectMapper.treeToValue(armourNode, UnderArmour.class);
			}
			else if (weaponType.equals(ArmourType.ARMOUR)) {
				return objectMapper.treeToValue(armourNode, OverArmourPiece.class);
			}
			else if (weaponType.equals(ArmourType.POWER_ARMOUR)) {
				return objectMapper.treeToValue(armourNode, PowerArmourPiece.class);
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
	 * @return A {@link Armour} object, which could be either a {@link UnderArmour}, {@link OverArmourPiece} or {@link PowerArmourPiece}, or {@code null} if the armourNode type is not recognized.
	 */
	private Armour createFromDocument(Document document)
	{
		if (document == null)
		{
			return null;
		}

		document.remove("_class");

		JsonNode jsonNode = objectMapper.valueToTree(document);
		return createFromJsonNode(jsonNode);
	}
}

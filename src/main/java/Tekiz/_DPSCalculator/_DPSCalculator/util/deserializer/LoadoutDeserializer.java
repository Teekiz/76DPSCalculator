package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

/**
 * A component used to deserialize {@link Loadout} objects.
 */
@Slf4j
@JsonComponent
public class LoadoutDeserializer extends JsonDeserializer<Loadout>
{
	/**
	 * A method used to deserialize a {@link Loadout} object.
	 * @param jsonParser The {@link JsonParser} providing the JSON input as a string.
	 * @param context The context for deserialization. Used for dependency injection.
	 * @return A {@link Loadout} object.
	 * @throws IOException If the JSON string cannot be parsed.
	 */
	@Override
	public Loadout deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException
	{
		ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
		JsonNode loadoutNode = jsonParser.getCodec().readTree(jsonParser);

		WeaponFactory weaponFactory = (WeaponFactory) context.findInjectableValue(WeaponFactory.class.getName(), null, null);
		LoadoutFactory loadoutFactory = (LoadoutFactory) context.findInjectableValue(LoadoutFactory.class.getName(), null, null);

		try
		{
			int loadoutID = objectMapper.treeToValue(loadoutNode.get("loadoutID"), int.class);
			Weapon weapon = weaponFactory.createObject(loadoutNode.get("weapon"));
			Player player = objectMapper.treeToValue(loadoutNode.get("player"), Player.class);
			Environment environment = objectMapper.treeToValue(loadoutNode.get("environment"), Environment.class);
			EquippedArmour armour = objectMapper.treeToValue(loadoutNode.get("armour"), EquippedArmour.class);

			JavaType perkHashMapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, Perk.class, Boolean.class);
			HashMap<Perk, Boolean> perks = objectMapper.treeToValue(loadoutNode.get("perks"), perkHashMapType);

			JavaType consumableHashMapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, Consumable.class, Boolean.class);
			HashMap<Consumable, Boolean> consumables = objectMapper.treeToValue(loadoutNode.get("consumables"), consumableHashMapType);

			Set<Mutation> mutations = objectMapper.convertValue(loadoutNode.get("mutations"), new TypeReference<Set<Mutation>>() {});

			Loadout loadout = loadoutFactory.createNewLoadout(loadoutID, weapon, perks, consumables, armour, player, environment, mutations);

			log.debug("Deserialized new loadout: {}", loadout);
			return loadout;
		}
		catch (Exception e)
		{
			log.error("Cannot deserialize loadout {}. JSON: {}. Message: {}.", loadoutNode.get("loadoutID"), loadoutNode, e.getMessage(), e);
			return null;
		}
	}
}

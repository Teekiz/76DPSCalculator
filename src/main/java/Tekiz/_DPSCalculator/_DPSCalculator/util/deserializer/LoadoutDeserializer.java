package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadoutDeserializer extends JsonDeserializer<Loadout>
{
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final WeaponFactory weaponFactory = new WeaponFactory(objectMapper);
	private final LoadoutFactory loadoutFactory = new LoadoutFactory();

	@Override
	public Loadout deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException
	{
		JsonNode loadoutNode = jsonParser.getCodec().readTree(jsonParser);
		Weapon weapon = weaponFactory.createWeapon(loadoutNode.get("weapon"));
		Player player = objectMapper.treeToValue(loadoutNode.get("player"), Player.class);
		Environment environment = objectMapper.treeToValue(loadoutNode.get("environment"), Environment.class);

		HashMap<Perk, Boolean> perks = objectMapper.treeToValue(loadoutNode.get("perks"), HashMap.class);
		HashMap<Consumable, Boolean> consumables = objectMapper.treeToValue(loadoutNode.get("consumables"), HashMap.class);

		Set<Armour> armour = objectMapper.treeToValue(loadoutNode.get("armour"), Set.class);
		Set<Mutation> mutations = objectMapper.treeToValue(loadoutNode.get("mutations"), Set.class);

		Loadout loadout = loadoutFactory.createNewLoadout(weapon, perks, consumables, armour, player, environment, mutations);

		log.debug("Deserialized new loadout: {}", loadout);
		return loadout;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ArmourLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ArmourModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.MutationLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.WeaponLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ObjectSerializationTest
{
	@Autowired
	ConsumableLoaderService consumableLoaderService;
	@Autowired
	PerkLoaderService perkLoaderService;
	@Autowired
	MutationLoaderService mutationLoaderService;
	@Autowired
	WeaponLoaderService weaponLoaderService;
	@Autowired
	WeaponFactory weaponFactory;
	@Autowired
	ArmourLoaderService armourLoaderService;
	@Autowired
	ArmourModLoaderService armourModLoaderService;
	@Autowired
	LoadoutFactory loadoutFactory;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	PerkManager perkManager;

	@Test
	public void serializeAndDeserializeConsumable() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeConsumable in ObjectSerializationTest.", System.lineSeparator());
		Consumable consumable = consumableLoaderService.getConsumable("TESTEVENT");
		assertNotNull(consumable);
		log.debug("Consumable object deserialized: {}.", consumable);

		String jsonConsumable = objectMapper.writeValueAsString(consumable);
		assertNotNull(jsonConsumable);
		log.debug("Consumable object serialized: {}.", jsonConsumable);

		Consumable newConsumable = objectMapper.readValue(jsonConsumable, Consumable.class);
		assertNotNull(newConsumable);
		log.debug("Consumable object deserialized: {}.", newConsumable);
	}

	@Test
	public void serializeAndDeserializePerk() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializePerk in ObjectSerializationTest.", System.lineSeparator());
		Perk perk = perkLoaderService.getPerk("GUNSLINGER");
		assertNotNull(perk);
		log.debug("Perk object deserialized: {}.", perk);

		String jsonPerk = objectMapper.writeValueAsString(perk);
		assertNotNull(jsonPerk);
		log.debug("Perk object serialized: {}.", jsonPerk);

		Perk newPerk = objectMapper.readValue(jsonPerk, Perk.class);
		assertNotNull(newPerk);
		log.debug("Perk object deserialized: {}.", newPerk);
	}

	@Test
	public void serializeAndDeserializeMutation() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeMutation in ObjectSerializationTest.", System.lineSeparator());
		Mutation mutation = mutationLoaderService.getMutation("ADRENALREACTION");
		assertNotNull(mutation);
		log.debug("Mutation object deserialized: {}.", mutation);

		String jsonMutation = objectMapper.writeValueAsString(mutation);
		assertNotNull(jsonMutation);
		log.debug("Mutation object serialized: {}.", jsonMutation);

		Mutation newMutation = objectMapper.readValue(jsonMutation, Mutation.class);
		assertNotNull(newMutation);
		log.debug("Mutation object deserialized: {}.", newMutation);
	}

	@Test
	public void serializeAndDeserializePlayer() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializePlayer in ObjectSerializationTest.", System.lineSeparator());
		Player player = new Player();
		assertNotNull(player);

		String jsonPlayer = objectMapper.writeValueAsString(player);
		assertNotNull(jsonPlayer);
		log.debug("Player object serialized: {}.", jsonPlayer);

		Player newPlayer = objectMapper.readValue(jsonPlayer, Player.class);
		assertNotNull(newPlayer);
		log.debug("Player object deserialized: {}.", newPlayer);
	}

	@Test
	public void serializeAndDeserializeWeapon() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeWeapon in ObjectSerializationTest.", System.lineSeparator());
		Weapon weapon = weaponLoaderService.getWeapon("10MMPISTOL");
		assertNotNull(weapon);
		log.debug("Weapon object deserialized: {}.", weapon);

		String jsonWeapon = objectMapper.writeValueAsString(weapon);
		assertNotNull(jsonWeapon);
		log.debug("Weapon object serialized: {}.", jsonWeapon);

		JsonNode weaponNode = objectMapper.readTree(jsonWeapon);
		Weapon newWeapon = weaponFactory.createWeapon(weaponNode);
		assertNotNull(newWeapon);
		log.debug("Weapon object deserialized: {}.", newWeapon);
	}

	@Test
	public void serializeAndDeserializeEnvironment() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeEnvironment in ObjectSerializationTest.", System.lineSeparator());
		Environment environment = new Environment();
		assertNotNull(environment);

		String jsonEnvironment = objectMapper.writeValueAsString(environment);
		assertNotNull(jsonEnvironment);
		log.debug("Environment object serialized: {}.", jsonEnvironment);

		Environment newEnvironment = objectMapper.readValue(jsonEnvironment, Environment.class);
		assertNotNull(newEnvironment);
		log.debug("Environment object deserialized: {}.", newEnvironment);
	}

	@Test
	public void serializeAndDeserializeArmour() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeArmour in ObjectSerializationTest.", System.lineSeparator());
		Armour armour = armourLoaderService.getArmour("WOODCHEST");
		Material material = armourModLoaderService.getMaterial("BOILEDLEATHERCHEST");
		armour.setMod(material);
		assertNotNull(armour);
		log.debug("Armour object deserialized: {}.", armour);

		String jsonArmour = objectMapper.writeValueAsString(armour);
		assertNotNull(jsonArmour);
		log.debug("Armour object serialized: {}.", jsonArmour);

		Armour newArmour = objectMapper.readValue(jsonArmour, Armour.class);
		assertNotNull(newArmour);
		log.debug("Armour object deserialized: {}.", newArmour);
	}

	@Test
	public void serializeAndDeserializeLoadout() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeLoadout in ObjectSerializationTest.", System.lineSeparator());
		Loadout loadout = loadoutFactory.createNewLoadout(1);
		assertNotNull(loadout);
		log.debug("Loadout object deserialized: {}.", loadout);

		String jsonLoadout = objectMapper.writeValueAsString(loadout);
		assertNotNull(jsonLoadout);
		log.debug("Loadout object serialized: {}.", jsonLoadout);

		Loadout newLoadout = objectMapper.readValue(jsonLoadout, Loadout.class);
		assertNotNull(newLoadout);
		log.debug("Loadout object deserialized: {}.", newLoadout);
	}

	@Test
	public void serializeAndDeserializeLoadoutWithHashMaps() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeLoadoutWithHashMaps in ObjectSerializationTest.", System.lineSeparator());
		Loadout loadout = loadoutFactory.createNewLoadout(1);
		assertNotNull(loadout);
		log.debug("Loadout object deserialized: {}.", loadout);

		perkManager.addPerk("GUNSLINGER", loadout);
		consumableManager.addConsumable("TESTCONDITION", loadout);

		String jsonLoadout = objectMapper.writeValueAsString(loadout);
		assertNotNull(jsonLoadout);
		log.debug("Loadout object serialized: {}.", jsonLoadout);

		Loadout newLoadout = objectMapper.readValue(jsonLoadout, Loadout.class);
		assertNotNull(newLoadout);
		log.debug("Loadout object deserialized: {}.", newLoadout);
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ObjectSerializationTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;
	@Autowired
	WeaponFactory weaponFactory;
	@Autowired
	LoadoutFactory loadoutFactory;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	PerkManager perkManager;

	@Test
	public void serializeAndDeserializePerk() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializePerk in ObjectSerializationTest.", System.lineSeparator());
		Perk perk = dataLoaderService.loadData("PERKS1", Perk.class, null);
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
	public void serializeAndDeserializeConsumable() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeConsumable in ObjectSerializationTest.", System.lineSeparator());
		Consumable consumable = dataLoaderService.loadData("CONSUMABLES7", Consumable.class, null);
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
	public void serializeAndDeserializeMutation() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeMutation in ObjectSerializationTest.", System.lineSeparator());
		Mutation mutation = dataLoaderService.loadData("MUTATIONS1", Mutation.class, null);
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
		Weapon weapon = dataLoaderService.loadData("WEAPONS2", Weapon.class, weaponFactory);
		assertNotNull(weapon);
		log.debug("Weapon object deserialized: {}.", weapon);

		String jsonWeapon = objectMapper.writeValueAsString(weapon);
		assertNotNull(jsonWeapon);
		log.debug("Weapon object serialized: {}.", jsonWeapon);

		JsonNode weaponNode = objectMapper.readTree(jsonWeapon);
		Weapon newWeapon = weaponFactory.createObject(weaponNode);
		assertNotNull(newWeapon);
		log.debug("Weapon object deserialized: {}.", newWeapon);
	}

	@Test
	public void serializeAndDeserializeWeapon_WithLegendaryEffect() throws IOException
	{
		log.debug("{}Running test - serializeAndDeserializeWeapon in ObjectSerializationTest.", System.lineSeparator());
		Weapon weapon = dataLoaderService.loadData("WEAPONS2", Weapon.class, weaponFactory);
		LegendaryEffect legendaryEffect = dataLoaderService.loadData("LEGENDARYEFFECT1", LegendaryEffect.class, null);
		weapon.getLegendaryEffects().addLegendaryEffect(legendaryEffect);

		assertNotNull(weapon);
		log.debug("Weapon object deserialized: {}.", weapon);

		String jsonWeapon = objectMapper.writeValueAsString(weapon);
		assertNotNull(jsonWeapon);
		log.debug("Weapon object serialized: {}.", jsonWeapon);

		JsonNode weaponNode = objectMapper.readTree(jsonWeapon);
		Weapon newWeapon = weaponFactory.createObject(weaponNode);
		assertNotNull(newWeapon);
		assertEquals(1, weapon.getLegendaryEffects().size());
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
		Armour armour = dataLoaderService.loadData("ARMOUR1", Armour.class, null);
		Material material = dataLoaderService.loadData("MODARMOURMATERIALS1", Material.class, null);
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

		perkManager.addPerk("PERKS1", loadout);
		consumableManager.addConsumable("CONSUMABLES6", loadout);//TESTCONDITION

		String jsonLoadout = objectMapper.writeValueAsString(loadout);
		assertNotNull(jsonLoadout);
		log.debug("Loadout object serialized: {}.", jsonLoadout);

		Loadout newLoadout = objectMapper.readValue(jsonLoadout, Loadout.class);
		assertNotNull(newLoadout);
		log.debug("Loadout object deserialized: {}.", newLoadout);
	}
}

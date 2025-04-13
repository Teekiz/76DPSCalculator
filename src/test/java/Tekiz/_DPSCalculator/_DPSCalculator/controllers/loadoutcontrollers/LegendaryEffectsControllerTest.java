package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadouts.LegendaryEffectController;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.PowerArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LegendaryEffectManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LegendaryEffectMapper;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ModifierMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LegendaryEffectController.class)
public class LegendaryEffectsControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@SpyBean
	LegendaryEffectManager legendaryEffectManager;
	@SpyBean
	LegendaryEffectMapper legendaryEffectMapper;
	@SpyBean
	ModifierMapper modifierMapper;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	DataLoaderService loaderService;

	String urlString = "/api/loadouts";

	//region <getAvailableLegendaryEffects>
	@Test
	public void getAvailableLegendaryEffects() throws Exception
	{
		log.debug("{}Running test - getAvailableLegendaryEffects in LegendaryEffectsControllerTest.", System.lineSeparator());
		when(loaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableLegendaryEffects")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "1",
			"name": "TEST",
			"description": "DESC1",
			"categories": ["ARMOUR"],
			"starType": "_1STAR"
		  },
		  {
			"id": "2",
			"name": "TEST2",
			"description": "DESC2",
			"categories": ["MELEE_WEAPONS", "RANGED_WEAPONS"],
			"starType": "_1STAR"
		  },
		  {
			"id": "3",
			"name": "TEST3",
			"description": "DESC3",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_1STAR"
		  },
		  {
			"id": "4",
			"name": "TEST4",
			"description": "DESC4",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_2STAR"
		  },
		  {
			"id": "5",
			"name": "TEST5",
			"description": "DESC5",
			"categories": ["RANGED_WEAPONS"],
			"starType": "_2STAR"
		  }
		]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableLegendaryEffects_withStarType() throws Exception
	{
		log.debug("{}Running test - getAvailableLegendaryEffects_withStarType in LegendaryEffectsControllerTest.", System.lineSeparator());
		when(loaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableLegendaryEffects")
					.param("starType", "_1STAR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "1",
			"name": "TEST",
			"description": "DESC1",
			"categories": ["ARMOUR"],
			"starType": "_1STAR"
		  },
		  {
			"id": "2",
			"name": "TEST2",
			"description": "DESC2",
			"categories": ["MELEE_WEAPONS", "RANGED_WEAPONS"],
			"starType": "_1STAR"
		  },
		  {
			"id": "3",
			"name": "TEST3",
			"description": "DESC3",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_1STAR"
		  }
		]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableLegendaryEffects_withCategory() throws Exception
	{
		log.debug("{}Running test - getAvailableLegendaryEffects_withCategory in LegendaryEffectsControllerTest.", System.lineSeparator());
		when(loaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableLegendaryEffects")
					.param("category", "RANGED_WEAPONS")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "2",
			"name": "TEST2",
			"description": "DESC2",
			"categories": ["MELEE_WEAPONS", "RANGED_WEAPONS"],
			"starType": "_1STAR"
		  },
		  {
			"id": "5",
			"name": "TEST5",
			"description": "DESC5",
			"categories": ["RANGED_WEAPONS"],
			"starType": "_2STAR"
		  }
		]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableLegendaryEffects_withStarType_withCategory() throws Exception
	{
		log.debug("{}Running test - getAvailableLegendaryEffects_withStarType_withCategory in LegendaryEffectsControllerTest.", System.lineSeparator());
		when(loaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableLegendaryEffects")
					.param("category", "ARMOUR")
					.param("starType", "_1STAR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "1",
			"name": "TEST",
			"description": "DESC1",
			"categories": ["ARMOUR"],
			"starType": "_1STAR"
		  },
		  {
			"id": "3",
			"name": "TEST3",
			"description": "DESC3",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_1STAR"
		  }
		]
		""";
		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableLegendaryEffects_withEmptyList() throws Exception
	{
		log.debug("{}Running test - getAvailableLegendaryEffects_withEmptyList in LegendaryEffectsControllerTest.", System.lineSeparator());
		when(loaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableLegendaryEffects")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[]
		""";
		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}
	//endregion

	//region <getWeaponLegendaryEffects>
	@Test
	public void getWeaponLegendaryEffects() throws Exception
	{
		log.debug("{}Running test - getWeaponLegendaryEffects in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(1), true);
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(4), true);

		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponLegendaryEffects")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "2",
			"name": "TEST2",
			"description": "DESC2",
			"categories": ["MELEE_WEAPONS", "RANGED_WEAPONS"],
			"starType": "_1STAR"
		  },
		  {
			"id": "5",
			"name": "TEST5",
			"description": "DESC5",
			"categories": ["RANGED_WEAPONS"],
			"starType": "_2STAR"
		  }
		]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getWeaponLegendaryEffects_WeaponIsNull() throws Exception
	{
		log.debug("{}Running test - getWeaponLegendaryEffects_WeaponIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		when(loadout.getWeapon()).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponLegendaryEffects")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
	}

	@Test
	public void getWeaponLegendaryEffects_WeaponHasNoEffects() throws Exception
	{
		log.debug("{}Running test - getWeaponLegendaryEffects_WeaponHasNoEffects in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponLegendaryEffects")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}
	//endregion

	//region <getArmourLegendaryEffects>
	@Test
	public void getArmourLegendaryEffects() throws Exception
	{
		log.debug("{}Running test - getArmourLegendaryEffects in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(2), true);
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(3), true);

		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourLegendaryEffects")
					.param("loadoutID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[
		  {
			"id": "3",
			"name": "TEST3",
			"description": "DESC3",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_1STAR"
		  },
		  {
			"id": "4",
			"name": "TEST4",
			"description": "DESC4",
			"categories": ["ARMOUR", "POWER_ARMOUR"],
			"starType": "_2STAR"
		  }
		]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getArmourLegendaryEffects_ArmourIsNull() throws Exception
	{
		log.debug("{}Running test - getArmourLegendaryEffects_ArmourIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourLegendaryEffects")
					.param("loadoutID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
	}

	@Test
	public void getArmourLegendaryEffects_ArmourHasNoEffects() throws Exception
	{
		log.debug("{}Running test - getArmourLegendaryEffects_ArmourHasNoEffects in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourLegendaryEffects")
					.param("loadoutID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
		[]
		""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}
	//endregion

	//region <addAndRemoveWeapon>
	@Test
	public void addWeaponLegendaryEffect() throws Exception
	{
		log.debug("{}Running test - addWeaponLegendaryEffect in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest().get(1));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "Legendary effect 1 has been applied to weapon in loadout 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addWeaponLegendaryEffect_WeaponIsNull() throws Exception
	{
		log.debug("{}Running test - addWeaponLegendaryEffect_WeaponIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		when(loadout.getWeapon()).thenReturn(null);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest().get(1));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Cannot add effect to object. Effect ID: 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addWeaponLegendaryEffect_EffectIsNull() throws Exception
	{
		log.debug("{}Running test - addWeaponLegendaryEffect_EffectIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);
		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Cannot add effect to object. Effect ID: 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeWeaponLegendaryEffect() throws Exception
	{
		log.debug("{}Running test - removeWeaponLegendaryEffect in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(1), true);

		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "_1STAR legendary effect has been removed from weapon in loadout 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeWeaponLegendaryEffect_WeaponDoesNotContainStar() throws Exception
	{
		log.debug("{}Running test - removeWeaponLegendaryEffect_WeaponDoesNotContainStar in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();

		Weapon weapon = RangedWeapon.builder().id("1").legendaryEffects(legendaryEffectsMap).build();
		when(loadout.getWeapon()).thenReturn(weapon);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		String expectedJson = "Weapon legendary effect could not be removed.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeWeaponLegendaryEffect_WeaponIsNull() throws Exception
	{
		log.debug("{}Running test - removeWeaponLegendaryEffect_WeaponIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);
		when(loadout.getWeapon()).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeWeaponLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Could not remove effect from object. Star type: _1STAR.";
		assertEquals(expectedJson, response.getContentAsString());
	}
	//endregion

	//region <addAndRemoveArmour>
	@Test
	public void addArmourLegendaryEffect() throws Exception
	{
		log.debug("{}Running test - addArmourLegendaryEffect in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();

		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest().get(2));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "Legendary effect 1 has been applied to POWER_ARMOUR LEFT_ARM in loadout 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addArmourLegendaryEffect_ArmourIsNull() throws Exception
	{
		log.debug("{}Running test - addArmourLegendaryEffect_ArmourIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(getLegendaryEffectsForTest().get(2));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Cannot add effect to object. Effect ID: 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addArmourLegendaryEffect_EffectIsNull() throws Exception
	{
		log.debug("{}Running test - addArmourLegendaryEffect_EffectIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();

		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		when(loaderService.loadData("1", LegendaryEffect.class, null)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("legendaryEffectID", "1")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Cannot add effect to object. Effect ID: 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeArmourLegendaryEffect() throws Exception
	{
		log.debug("{}Running test - removeArmourLegendaryEffect in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(getLegendaryEffectsForTest().get(2), true);

		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "_1STAR legendary effect has been removed from POWER_ARMOUR LEFT_ARM in loadout 1.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeArmourLegendaryEffect_ArmourDoesNotContainStar() throws Exception
	{
		log.debug("{}Running test - removeArmourLegendaryEffect_ArmourDoesNotContainStar in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		PowerArmourPiece armourPiece = PowerArmourPiece.builder().id("1").armourPiece(ArmourPiece.ARMS).armourType(ArmourType.POWER_ARMOUR).armourSlot(ArmourSlot.LEFT_ARM).legendaryEffects(legendaryEffectsMap).build();
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(armourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		String expectedJson = "Armour legendary effect could not be removed.";
		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void removeArmourLegendaryEffect_ArmourIsNull() throws Exception
	{
		log.debug("{}Running test - removeArmourLegendaryEffect_ArmourIsNull in LegendaryEffectsControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		when(loadoutManager.getLoadout(1)).thenReturn(loadout);
		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeArmourLegendaryEffect")
					.param("loadoutID", "1")
					.param("starType", "_1STAR")
					.param("armourType", "POWER_ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Could not remove effect from object. Star type: _1STAR.";
		assertEquals(expectedJson, response.getContentAsString());
	}
	//endregion

	//region <utility>
	/**
	 * A utility method for getting legendary effects.
	 * @return A list of legendary effects.
	 */
	private List<LegendaryEffect> getLegendaryEffectsForTest(){
		LegendaryEffect legendaryEffectOne = new LegendaryEffect("1", "TEST", "DESC1", ModifierSource.LEGENDARY_EFFECT, List.of(Category.ARMOUR), StarType._1STAR, null, null);
		LegendaryEffect legendaryEffectTwo = new LegendaryEffect("2", "TEST2", "DESC2", ModifierSource.LEGENDARY_EFFECT, List.of(Category.MELEE_WEAPONS, Category.RANGED_WEAPONS), StarType._1STAR, null, null);
		LegendaryEffect legendaryEffectThree = new LegendaryEffect("3", "TEST3", "DESC3", ModifierSource.LEGENDARY_EFFECT, List.of(Category.ARMOUR, Category.POWER_ARMOUR), StarType._1STAR, null, null);
		LegendaryEffect legendaryEffectFour = new LegendaryEffect("4", "TEST4", "DESC4", ModifierSource.LEGENDARY_EFFECT, List.of(Category.ARMOUR, Category.POWER_ARMOUR), StarType._2STAR, null, null);
		LegendaryEffect legendaryEffectFive = new LegendaryEffect("5", "TEST5", "DESC5", ModifierSource.LEGENDARY_EFFECT, List.of(Category.RANGED_WEAPONS), StarType._2STAR, null, null);
		return List.of(legendaryEffectOne, legendaryEffectTwo, legendaryEffectThree, legendaryEffectFour, legendaryEffectFive);

	}
	//endregion
}

package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadouts.ArmourController;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.EquippedArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.OverArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.PowerArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.UnderArmour;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ArmourFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ArmourMapper;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ModifierMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ArmourController.class)
public class ArmourControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@SpyBean
	ArmourManager armourManager;
	@SpyBean
	ArmourMapper armourMapper;
	@SpyBean
	ModifierMapper modifierMapper;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	DataLoaderService armourLoaderService;
	@MockBean
	ArmourFactory armourFactory;

	String urlString = "/api/loadouts";

	//region <getArmour>
	@Test
	public void getArmour() throws Exception
	{
		log.debug("{}Running test - getArmour in ArmourControllerTest.", System.lineSeparator());
		UnderArmour underArmour = UnderArmour.builder().id("1").name("TEST").armourType(ArmourType.UNDER_ARMOUR).armourPiece(ArmourPiece.ALL).armourSlot(ArmourSlot.ALL).build();
		PowerArmourPiece powerArmourPiece = PowerArmourPiece.builder().id("2").name("TESTPA").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.TORSO).armourSlot(ArmourSlot.TORSO).build();
		EquippedArmour armour = new EquippedArmour();
		armour.addArmour(underArmour);
		armour.addArmour(powerArmourPiece);

		Loadout loadout = mock(Loadout.class);
		when(loadout.getArmour()).thenReturn(armour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmour")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
			  "id": "1",
			  "name": "TEST",
			  "armourType": "UNDER_ARMOUR",
			  "armourPiece" : "ALL",
			  "armourSlot" : "ALL"
			},
			{
			  "id": "2",
			  "name": "TESTPA",
			  "armourType": "POWER_ARMOUR",
			  "armourPiece" : "TORSO",
			  "armourSlot" : "TORSO"
			}
			]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(armourMapper, times(1)).convertEquippedArmour(armour);
	}

	@Test
	public void getArmour_NoArmourEquipped() throws Exception
	{
		log.debug("{}Running test - getArmour_NoArmourEquipped in ArmourControllerTest.", System.lineSeparator());
		EquippedArmour armour = new EquippedArmour();

		Loadout loadout = mock(Loadout.class);
		when(loadout.getArmour()).thenReturn(armour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmour")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();


		JSONAssert.assertEquals("[]", response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(armourMapper, times(1)).convertEquippedArmour(armour);
	}

	@Test
	public void getArmour_EquippedArmourIsNull() throws Exception
	{
		log.debug("{}Running test - getArmour_EquippedArmourIsNull in ArmourControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getArmour()).thenReturn(null);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmour")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
		verify(loadoutManager, times(1)).getLoadout(1);
	}

	//endregion

	//region <addArmour>
	@Test
	public void addArmour() throws Exception
	{
		log.debug("{}Running test - addArmour in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).build();
		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmour")
					.param("loadoutID", "1")
					.param("armourID", "1")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "1 has been added to slot LEFT_ARM.";

		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addArmour_withIncorrectID() throws Exception
	{
		log.debug("{}Running test - addArmour_withIncorrectID in ArmourControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmour")
					.param("loadoutID", "1")
					.param("armourID", "1")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedJson = "Armour not found while adding armour. Armour ID: 1.";

		assertEquals(expectedJson, response.getContentAsString());
	}

	@Test
	public void addArmour_withInvalidSlot() throws Exception
	{
		log.debug("{}Running test - addArmour_withInvalidSlot in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).build();
		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addArmour")
					.param("loadoutID", "1")
					.param("armourID", "1")
					.param("armourSlot", "TORSO")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		String expectedJson = "Armour could not be applied.";

		assertEquals(expectedJson, response.getContentAsString());
	}
	//endregion

	//region <getAvailableArmour>
	@Test
	public void getAvailableArmour() throws Exception
	{
		log.debug("{}Running test - getAvailableArmour in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		OverArmourPiece overArmourPieceTwo = OverArmourPiece.builder().id("2").name("TESTTWO").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		UnderArmour underArmour = UnderArmour.builder().id("3").name("TESTTHREE").armourType(ArmourType.UNDER_ARMOUR).armourPiece(ArmourPiece.ALL).build();
		PowerArmourPiece powerArmourPiece = PowerArmourPiece.builder().id("4").name("TESTFOUR").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.HELMET).build();

		List<Armour> armourList = new ArrayList<>(Arrays.asList(overArmourPiece, overArmourPieceTwo, underArmour, powerArmourPiece));

		when(armourLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)).thenReturn(armourList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmour")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
			  "id": "1",
			  "name": "TEST",
			  "armourType": "ARMOUR",
			  "armourPiece" : "ARMS"
			},
			{
			  "id": "2",
			  "name": "TESTTWO",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			},
			{
			  "id": "3",
			  "name": "TESTTHREE",
			  "armourType": "UNDER_ARMOUR",
			  "armourPiece" : "ALL"
			},
			{
			  "id": "4",
			  "name": "TESTFOUR",
			  "armourType": "POWER_ARMOUR",
			  "armourPiece" : "HELMET"
			}
			]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmour_withArmourTypeSet() throws Exception
	{
		log.debug("{}Running test - getAvailableArmour_withArmourTypeSet in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		OverArmourPiece overArmourPieceTwo = OverArmourPiece.builder().id("2").name("TESTTWO").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		UnderArmour underArmour = UnderArmour.builder().id("3").name("TESTTHREE").armourType(ArmourType.UNDER_ARMOUR).armourPiece(ArmourPiece.ALL).build();
		PowerArmourPiece powerArmourPiece = PowerArmourPiece.builder().id("4").name("TESTFOUR").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.HELMET).build();

		List<Armour> armourList = new ArrayList<>(Arrays.asList(overArmourPiece, overArmourPieceTwo, underArmour, powerArmourPiece));

		when(armourLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)).thenReturn(armourList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmour")
					.param("armourType", "ARMOUR")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
			  "id": "1",
			  "name": "TEST",
			  "armourType": "ARMOUR",
			  "armourPiece" : "ARMS"
			},
			{
			  "id": "2",
			  "name": "TESTTWO",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			}]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmour_withArmourPieceSet() throws Exception
	{
		log.debug("{}Running test - getAvailableArmour_withArmourPieceSet in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		OverArmourPiece overArmourPieceTwo = OverArmourPiece.builder().id("2").name("TESTTWO").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		OverArmourPiece overArmourPieceThree = OverArmourPiece.builder().id("5").name("TESTFIVE").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		UnderArmour underArmour = UnderArmour.builder().id("3").name("TESTTHREE").armourType(ArmourType.UNDER_ARMOUR).armourPiece(ArmourPiece.ALL).build();
		PowerArmourPiece powerArmourPiece = PowerArmourPiece.builder().id("4").name("TESTFOUR").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.HELMET).build();
		PowerArmourPiece powerArmourPieceTwo = PowerArmourPiece.builder().id("6").name("TESTSIX").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.TORSO).build();

		List<Armour> armourList = new ArrayList<>(Arrays.asList(overArmourPiece, overArmourPieceTwo, overArmourPieceThree, underArmour, powerArmourPiece, powerArmourPieceTwo));

		when(armourLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)).thenReturn(armourList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmour")
					.param("armourPiece", "TORSO")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
			  "id": "2",
			  "name": "TESTTWO",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			},
			{
			  "id": "5",
			  "name": "TESTFIVE",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			},
			{
			  "id": "6",
			  "name": "TESTSIX",
			  "armourType": "POWER_ARMOUR",
			  "armourPiece" : "TORSO"
			}]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmour_withArmourTypeSet_withArmourPieceSet() throws Exception
	{
		log.debug("{}Running test - getAvailableArmour_withArmourTypeSet_withArmourPieceSet in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		OverArmourPiece overArmourPieceTwo = OverArmourPiece.builder().id("2").name("TESTTWO").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		OverArmourPiece overArmourPieceThree = OverArmourPiece.builder().id("5").name("TESTFIVE").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.TORSO).build();
		UnderArmour underArmour = UnderArmour.builder().id("3").name("TESTTHREE").armourType(ArmourType.UNDER_ARMOUR).armourPiece(ArmourPiece.ALL).build();
		PowerArmourPiece powerArmourPiece = PowerArmourPiece.builder().id("4").name("TESTFOUR").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.HELMET).build();
		PowerArmourPiece powerArmourPieceTwo = PowerArmourPiece.builder().id("6").name("TESTSIX").armourType(ArmourType.POWER_ARMOUR).armourPiece(ArmourPiece.TORSO).build();

		List<Armour> armourList = new ArrayList<>(Arrays.asList(overArmourPiece, overArmourPieceTwo, overArmourPieceThree, underArmour, powerArmourPiece, powerArmourPieceTwo));

		when(armourLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)).thenReturn(armourList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmour")
					.param("armourType", "ARMOUR")
					.param("armourPiece", "TORSO")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
			  "id": "2",
			  "name": "TESTTWO",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			},
			{
			  "id": "5",
			  "name": "TESTFIVE",
			  "armourType": "ARMOUR",
			  "armourPiece" : "TORSO"
			}]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmour_withEmptyReturn() throws Exception
	{
		log.debug("{}Running test - getAvailableArmour_withEmptyReturn in ArmourControllerTest.", System.lineSeparator());
		List<Armour> armourList = new ArrayList<>();

		when(armourLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)).thenReturn(armourList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmour")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();
		assertEquals("[]", response.getContentAsString());
	}
	//endregion

	//region <getArmourDetails>
	@Test
	public void getArmourDetails() throws Exception
	{
		log.debug("{}Running test - getArmourDetails in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourDetails")
					.param("armourID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			{
				"id":"1",
				"name":"TEST",
				"armourType":"ARMOUR",
				"armourPiece":"ARMS",
				"armourLevel":0,
				"armourSlot":null,
				"armourSet":null,
				"armourResistance":null,
				"armourClassification":null,
				"modifications":null
			}""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getArmourDetails_InvalidID() throws Exception
	{
		log.debug("{}Running test - getArmourDetails_InvalidID in ArmourControllerTest.", System.lineSeparator());
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourDetails")
					.param("armourID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertEquals("", response.getContentAsString());
	}
	//endregion

	//region <getAvailableArmourMods>
	@Test
	public void getAvailableArmourMods() throws Exception
	{
		log.debug("{}Running test - getAvailableArmourMods in ArmourControllerTest.", System.lineSeparator());

		ModificationSlot<ArmourMod> modificationSlot = new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TEST1", "TEST2"));
		ModificationSlot<ArmourMod> modificationSlotTwo = new ModificationSlot<>(null, ModType.MISCELLANEOUS, true, Set.of("TESTONE", "TESTTWO"));
		HashMap<ModType, ModificationSlot<ArmourMod>> modMap = new HashMap<>();
		modMap.put(ModType.MATERIAL, modificationSlot);
		modMap.put(ModType.MISCELLANEOUS, modificationSlotTwo);

		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).modifications(modMap).build();
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);
		when(armourLoaderService.loadAllData("ARMOURMODS", ArmourMod.class, null)).thenReturn(getArmourModsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmourMods")
					.param("armourID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
				"id":"1",
				"name":"MATMOD1"
			},
			{
				"id":"2",
				"name":"MATMOD2"
			},
			{
				"id":"3",
				"name":"MISCMOD1"
			},
			{
				"id":"4",
				"name":"MISCMOD2"
			}]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmourMods_withModType() throws Exception
	{
		log.debug("{}Running test - getAvailableArmourMods_withModType in ArmourControllerTest.", System.lineSeparator());

		ModificationSlot<ArmourMod> modificationSlot = new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TEST1", "TEST2"));
		ModificationSlot<ArmourMod> modificationSlotTwo = new ModificationSlot<>(null, ModType.MISCELLANEOUS, true, Set.of("TESTONE", "TESTTWO"));
		HashMap<ModType, ModificationSlot<ArmourMod>> modMap = new HashMap<>();
		modMap.put(ModType.MATERIAL, modificationSlot);
		modMap.put(ModType.MISCELLANEOUS, modificationSlotTwo);

		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).modifications(modMap).build();
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);
		when(armourLoaderService.loadAllData("ARMOURMODS", ArmourMod.class, null)).thenReturn(getArmourModsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmourMods")
					.param("armourID", "1")
					.param("modType", "MATERIAL")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[{
				"id":"1",
				"name":"MATMOD1"
			},
			{
				"id":"2",
				"name":"MATMOD2"
			}]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmourMods_invalidArmourID() throws Exception
	{
		log.debug("{}Running test - getAvailableArmourMods_withModType in ArmourControllerTest.", System.lineSeparator());

		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(null);
		when(armourLoaderService.loadAllData("ARMOURMODS", ArmourMod.class, null)).thenReturn(getArmourModsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmourMods")
					.param("armourID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getAvailableArmourMods_withNullModSlot() throws Exception
	{
		log.debug("{}Running test - getAvailableArmourMods_withModType in ArmourControllerTest.", System.lineSeparator());


		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).build();
		when(armourLoaderService.loadData("1", Armour.class, armourFactory)).thenReturn(overArmourPiece);
		when(armourLoaderService.loadAllData("ARMOURMODS", ArmourMod.class, null)).thenReturn(getArmourModsForTest());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableArmourMods")
					.param("armourID", "1")
					.param("modType", "MATERIAL")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			[]""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}
	//endregion

	//region <getArmourModDetails>
	@Test
	public void getArmourModDetails() throws Exception
	{
		log.debug("{}Running test - getArmourModDetails in ArmourControllerTest.", System.lineSeparator());
		HashMap<ModifierTypes, ModifierValue<?>> effects = new HashMap<>();
		effects.put(ModifierTypes.RELOADSPEED, new ModifierValue<>(ModifierTypes.RELOADSPEED, 0.55));
		ArmourMod armourMod = new ArmourMod("1", "MATMOD1", "TEST1", ModType.MATERIAL, null, null, null, null, effects);

		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(armourMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourModDetails")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		verify(armourLoaderService).loadData("1", ArmourMod.class, null);

		String expectedJson = """
		{
			"id":"1",
			"name":"MATMOD1",
			"modType":"MATERIAL",
			"modificationEffects":[
				{
					"type":"RELOADSPEED",
					"value":0.55,
					"userDescription":"Reload speed: +55%."
				}
			]
		}""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getArmourModDetails_InvalidModID() throws Exception
	{
		log.debug("{}Running test - getArmourModDetails_InvalidModID in ArmourControllerTest.", System.lineSeparator());
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getArmourModDetails")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		verify(armourLoaderService).loadData("1", ArmourMod.class, null);
		assertTrue(response.getContentAsString().isEmpty());
	}
	//endregion

	//region <modifyArmour>
	@Test
	public void modifyArmour() throws Exception
	{
		log.debug("{}Running test - modifyArmour in ArmourControllerTest.", System.lineSeparator());
		ModificationSlot<ArmourMod> modificationSlot = new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TEST1", "TEST2"));
		ModificationSlot<ArmourMod> modificationSlotTwo = new ModificationSlot<>(null, ModType.MISCELLANEOUS, true, Set.of("TESTONE", "TESTTWO"));
		HashMap<ModType, ModificationSlot<ArmourMod>> modMap = new HashMap<>();
		modMap.put(ModType.MATERIAL, modificationSlot);
		modMap.put(ModType.MISCELLANEOUS, modificationSlotTwo);
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).modifications(modMap).build();

		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(overArmourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(new ArmourMod("1", "TEST", "TEST1", ModType.MATERIAL, ModSubType.NOT_APPLICABLE, ArmourPiece.ARMS, ModifierSource.ARMOUR_MODIFICATION, null, null));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/modifyArmour")
					.param("loadoutID", "1")
					.param("modID", "1")
					.param("armourType", "ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertEquals("Modification 1 has been applied to ARMOUR LEFT_ARM in loadout 1.", response.getContentAsString());
		assertEquals(overArmourPiece.getModifications().get(ModType.MATERIAL).getCurrentModification().id(), "1");
	}

	@Test
	public void modifyArmour_InvalidArmourModID() throws Exception
	{
		log.debug("{}Running test - modifyArmour in ArmourControllerTest.", System.lineSeparator());
		ModificationSlot<ArmourMod> modificationSlot = new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TEST1", "TEST2"));
		ModificationSlot<ArmourMod> modificationSlotTwo = new ModificationSlot<>(null, ModType.MISCELLANEOUS, true, Set.of("TESTONE", "TESTTWO"));
		HashMap<ModType, ModificationSlot<ArmourMod>> modMap = new HashMap<>();
		modMap.put(ModType.MATERIAL, modificationSlot);
		modMap.put(ModType.MISCELLANEOUS, modificationSlotTwo);
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).modifications(modMap).build();

		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(overArmourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/modifyArmour")
					.param("loadoutID", "1")
					.param("modID", "1")
					.param("armourType", "ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		assertEquals("Armour or mod not found during armour modification. Modification ID: 1. Armour piece: LEFT_ARM for ARMOUR.", response.getContentAsString());
	}

	@Test
	public void modifyArmour_InvalidModForType() throws Exception
	{
		log.debug("{}Running test - modifyArmour_InvalidModForType in ArmourControllerTest.", System.lineSeparator());
		ModificationSlot<ArmourMod> modificationSlot = new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TEST1", "TEST2"));
		ModificationSlot<ArmourMod> modificationSlotTwo = new ModificationSlot<>(null, ModType.MISCELLANEOUS, true, Set.of("TESTONE", "TESTTWO"));
		HashMap<ModType, ModificationSlot<ArmourMod>> modMap = new HashMap<>();
		modMap.put(ModType.MATERIAL, modificationSlot);
		modMap.put(ModType.MISCELLANEOUS, modificationSlotTwo);
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).modifications(modMap).build();

		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(overArmourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(new ArmourMod("1", "TEST", "TEST1", ModType.MATERIAL, ModSubType.NOT_APPLICABLE, ArmourPiece.TORSO, ModifierSource.ARMOUR_MODIFICATION, null, null));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/modifyArmour")
					.param("loadoutID", "1")
					.param("modID", "1")
					.param("armourType", "ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		assertEquals("Armour mod could not be applied.", response.getContentAsString());
	}

	@Test
	public void modifyArmour_ModSlotIsNull() throws Exception
	{
		log.debug("{}Running test - modifyArmour_ModSlotIsNull in ArmourControllerTest.", System.lineSeparator());
		OverArmourPiece overArmourPiece = OverArmourPiece.builder().id("1").name("TEST").armourType(ArmourType.ARMOUR).armourPiece(ArmourPiece.ARMS).armourSlot(ArmourSlot.LEFT_ARM).build();

		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		equippedArmour.addArmour(overArmourPiece);
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(new ArmourMod("1", "TEST", "TEST1", ModType.MATERIAL, ModSubType.NOT_APPLICABLE, ArmourPiece.TORSO, ModifierSource.ARMOUR_MODIFICATION, null, null));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/modifyArmour")
					.param("loadoutID", "1")
					.param("modID", "1")
					.param("armourType", "ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		assertEquals("Armour mod could not be applied.", response.getContentAsString());
	}

	@Test
	public void modifyArmour_ArmourIsNull() throws Exception
	{
		log.debug("{}Running test - modifyArmour_ArmourIsNull in ArmourControllerTest.", System.lineSeparator());
		Loadout loadout = mock(Loadout.class);
		EquippedArmour equippedArmour = new EquippedArmour();
		when(loadout.getArmour()).thenReturn(equippedArmour);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		when(armourLoaderService.loadData("1", ArmourMod.class, null)).thenReturn(new ArmourMod("1", "TEST", "TEST1", ModType.MATERIAL, ModSubType.NOT_APPLICABLE, ArmourPiece.TORSO, ModifierSource.ARMOUR_MODIFICATION, null, null));

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/modifyArmour")
					.param("loadoutID", "1")
					.param("modID", "1")
					.param("armourType", "ARMOUR")
					.param("armourSlot", "LEFT_ARM")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		assertEquals("Armour or mod not found during armour modification. Modification ID: 1. Armour piece: LEFT_ARM for ARMOUR.", response.getContentAsString());
	}
	//endregion

	//region <utility>
	/**
	 * A utility method for getting armour mods.
	 * @return A list of armour mods.
	 */
	private List<ArmourMod> getArmourModsForTest(){
		ArmourMod armourMod = new ArmourMod("1", "MATMOD1", "TEST1", ModType.MATERIAL, null, null, null, null, null);
		ArmourMod armourModTwo = new ArmourMod("2", "MATMOD2", "TEST2", ModType.MATERIAL, null, null, null, null, null);
		ArmourMod armourModThree = new ArmourMod("3", "MISCMOD1", "TESTONE", ModType.MISCELLANEOUS, null, null, null, null, null);
		ArmourMod armourModFour = new ArmourMod("4", "MISCMOD2", "TESTTwo", ModType.MISCELLANEOUS, null, null, null, null, null);
		return new ArrayList<>(Arrays.asList(armourMod, armourModTwo, armourModThree, armourModFour));
	}
	//endregion
}

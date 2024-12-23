package Tekiz._DPSCalculator._DPSCalculator.controllers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers.PerkController;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkRank;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PerkMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PerkController.class)
public class PerkControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	PerkManager perkManager;
	@MockBean
	PerkMapper perkMapper;
	@MockBean
	DataLoaderService perkLoaderService;

	String urlString = "/api/loadouts";

	@Test
	public void getPerks_WithPerksInList() throws Exception
	{
		log.debug("{}Running test - getPerks_WithList in PerkControllerTest.", System.lineSeparator());

		ExpressionParser parser = new SpelExpressionParser();
		Expression testExpression = parser.parseExpression("1 > 2");

		Perk perk = new Perk("TEST1", "TESTPERK", Specials.STRENGTH, new PerkRank(1, 1, 2), "", ModifierSource.PERK, testExpression, new HashMap<>());
		PerkDTO perkDTO = new PerkDTO("TEST1", "TESTPERK", Specials.STRENGTH.toString(), 1, 1, 2, "");
		List<PerkDTO> perkDTOS = new ArrayList<>();
		perkDTOS.add(perkDTO);

		Loadout loadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());
		loadout.getPerks().put(perk, true);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(perkMapper.convertAllToDTO(loadout.getPerks())).willReturn(perkDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getPerks")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\":\"TEST1\",\"name\":\"TESTPERK\",\"special\":\"STRENGTH\",\"currentRank\"" +
			":1,\"baseCost\":1,\"maxRank\":2,\"description\":\"\"}]:0"
			+ "}";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(perkMapper, times(1)).convertAllToDTO(loadout.getPerks());
	}

	@Test
	public void getPerks_WithEmptyPerksList() throws Exception
	{
		log.debug("{}Running test - getPerks_WithEmptyPerksList in PerkControllerTest.", System.lineSeparator());

		Loadout loadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(perkMapper.convertAllToDTO(loadout.getPerks())).willReturn(new ArrayList<>());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getPerks")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(perkMapper, times(1)).convertAllToDTO(loadout.getPerks());
	}

	@Test
	public void addPerk() throws Exception
	{
		log.debug("{}Running test - addPerk in PerkControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addPerk")
					.param("loadoutID", "1")
					.param("perkID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been added to your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(perkManager, times(1)).addPerk("1", mockLoadout);
	}

	@Test
	public void removePerk() throws Exception
	{
		log.debug("{}Running test - removePerk in PerkControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removePerk")
					.param("loadoutID", "1")
					.param("perkID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been removed from your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(perkManager, times(1)).removePerk("1", mockLoadout);
	}

	//getAvailablePerks
	@Test
	public void getAvailablePerks() throws Exception
	{
		log.debug("{}Running test - getAvailablePerks in PerkControllerTest.", System.lineSeparator());

		ExpressionParser parser = new SpelExpressionParser();
		Expression testExpression = parser.parseExpression("1 > 2");

		Perk perkOne = new Perk("TEST1", "TESTPERK1", Specials.STRENGTH, new PerkRank(1, 1, 2),
			"", ModifierSource.PERK, testExpression, new HashMap<>());
		Perk perkTwo = new Perk("TEST2", "TESTPERK2", Specials.CHARISMA, new PerkRank(1, 1, 2),
			"", ModifierSource.PERK, testExpression, new HashMap<>());

		List<Perk> availablePerks = new ArrayList<>();
		availablePerks.add(perkOne);
		availablePerks.add(perkTwo);

		PerkDTO perkDTOOne = new PerkDTO("TEST1", "TESTPERK1", Specials.STRENGTH.toString(), 1, 1, 2, "");
		PerkDTO perkDTOTwo = new PerkDTO("TEST2", "TESTPERK2", Specials.STRENGTH.toString(), 1, 1, 2, "");

		List<PerkDTO> availablePerkDTOS = new ArrayList<>();
		availablePerkDTOS.add(perkDTOOne);
		availablePerkDTOS.add(perkDTOTwo);


		given(perkLoaderService.loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(),
			ArgumentMatchers.any())).willReturn(Collections.singletonList(availablePerks));
		given(perkMapper.convertAllToDTO(anyList())).willReturn(availablePerkDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailablePerks")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\":\"TEST1\",\"name\":\"TESTPERK1\"," +
			"\"special\":\"STRENGTH\",\"currentRank\":1,\"baseCost\":1," +
			"\"maxRank\":2,\"description\":\"\"}," +
			"{\"id\":\"TEST2\",\"name\":\"TESTPERK2\"" +
			",\"special\":\"STRENGTH\",\"currentRank\":1,\"" +
			"baseCost\":1,\"maxRank\":2,\"description\":\"\"}]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(perkLoaderService, times(1)).loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
		verify(perkMapper, times(1)).convertAllToDTO(anyList());
	}

	//changePerkRank
	@Test
	public void changePerkRank() throws Exception
	{
		log.debug("{}Running test - changePerkRank in PerkControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/changePerkRank")
					.param("loadoutID", "1")
					.param("perkID", "1")
					.param("perkRank", "2")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1's rank has been modified."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(perkManager, times(1)).changePerkRank("1",2,  mockLoadout);
	}
}

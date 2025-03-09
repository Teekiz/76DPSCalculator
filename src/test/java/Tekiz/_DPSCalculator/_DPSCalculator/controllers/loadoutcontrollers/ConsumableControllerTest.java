package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers.ConsumableController;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.ConsumableDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.AddictionType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.consumables.ConsumableType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ConsumableMapper;
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
@WebMvcTest(ConsumableController.class)
public class ConsumableControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	ConsumableManager consumableManager;
	@MockBean
	ConsumableMapper consumableMapper;
	@MockBean
	DataLoaderService consumableLoaderService;

	String urlString = "/api/loadouts";

	@Test
	public void getConsumables_WithConsumablesInList() throws Exception
	{
		log.debug("{}Running test - getConsumables_WithConsumablesInList in ConsumableControllerTest.", System.lineSeparator());

		ExpressionParser parser = new SpelExpressionParser();
		Expression testExpression = parser.parseExpression("1 > 2");

		Consumable consumable = new Consumable("TEST1", "TESTCONSUMABLE", ConsumableType.ALCOHOL, AddictionType.ALCOHOL,
			ModifierSource.CONSUMABLE_ALCOHOL, testExpression, new HashMap<>());
		ConsumableDTO consumableDTO = new ConsumableDTO("TEST1", "TESTCONSUMABLE", ConsumableType.ALCOHOL.toString(), AddictionType.ALCOHOL.toString());
		List<ConsumableDTO> consumableDTOS = new ArrayList<>();
		consumableDTOS.add(consumableDTO);

		Loadout loadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>(), null);
		loadout.getConsumables().put(consumable, true);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(consumableMapper.convertAllToDTO(loadout.getConsumables())).willReturn(consumableDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getConsumables")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\":\"TEST1\"," +
			"\"name\":" + "\"TESTCONSUMABLE\"," +
			"\"consumableType\":" + "\"ALCOHOL\"," +
			"\"addictionType\"" + ":\"ALCOHOL\"}]:0"
			+ "}";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(consumableMapper, times(1)).convertAllToDTO(loadout.getConsumables());
	}

	@Test
	public void getConsumables_WithEmptyConsumablesList() throws Exception
	{
		log.debug("{}Running test - getConsumables_WithEmptyConsumablesList in ConsumableControllerTest.", System.lineSeparator());

		Loadout loadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>(), null);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(consumableMapper.convertAllToDTO(loadout.getConsumables())).willReturn(new ArrayList<>());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getConsumables")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(consumableMapper, times(1)).convertAllToDTO(loadout.getConsumables());
	}

	@Test
	public void addConsumable() throws Exception
	{
		log.debug("{}Running test - addConsumable in ConsumableControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>(), null);

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addConsumable")
					.param("loadoutID", "1")
					.param("consumableID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been added to your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(consumableManager, times(1)).addConsumable("1", mockLoadout);
	}

	@Test
	public void removeConsumable() throws Exception
	{
		log.debug("{}Running test - removeConsumable in ConsumableControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>(), null);

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeConsumable")
					.param("loadoutID", "1")
					.param("consumableID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been removed from your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(consumableManager, times(1)).removeConsumable("1", mockLoadout);
	}

	@Test
	public void getAvailableConsumables() throws Exception
	{
		log.debug("{}Running test - getAvailableConsumables in ConsumableControllerTest.", System.lineSeparator());

		ExpressionParser parser = new SpelExpressionParser();
		Expression testExpression = parser.parseExpression("1 > 2");

		Consumable consumableOne = new Consumable("TEST1", "TESTCONSUMABLEONE", ConsumableType.ALCOHOL, AddictionType.ALCOHOL,
			ModifierSource.CONSUMABLE_ALCOHOL, testExpression, new HashMap<>());
		Consumable consumableTwo = new Consumable("TEST1", "TESTCONSUMABLETWO", ConsumableType.CHEMS, AddictionType.PSYCHO,
			ModifierSource.CONSUMABLE_CHEMS, testExpression, new HashMap<>());

		ConsumableDTO consumableDTOOne = new ConsumableDTO("TEST1", "TESTCONSUMABLEONE", ConsumableType.ALCOHOL.toString(), AddictionType.ALCOHOL.toString());
		ConsumableDTO consumableDTOTwo = new ConsumableDTO("TEST2", "TESTCONSUMABLEONE", ConsumableType.CHEMS.toString(), AddictionType.PSYCHO.toString());

		List<Consumable> availableConsumables = new ArrayList<>();
		availableConsumables.add(consumableOne);
		availableConsumables.add(consumableTwo);

		List<ConsumableDTO> availableConsumablesDTOs = new ArrayList<>();
		availableConsumablesDTOs.add(consumableDTOOne);
		availableConsumablesDTOs.add(consumableDTOTwo);

		given(consumableLoaderService.loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(),
			ArgumentMatchers.any())).willReturn(Collections.singletonList(availableConsumables));
		given(consumableMapper.convertAllToDTO(anyList())).willReturn(availableConsumablesDTOs);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableConsumables")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{" +
			"\"id\":\"TEST1\"," +
			"\"name\":\"TESTCONSUMABLEONE\"," +
			"\"consumableType\":\"ALCOHOL\"," +
			"\"addictionType\":\"ALCOHOL\"" +
			"},{" +
			"\"id\":\"TEST2\"," +
			"\"name\":\"TESTCONSUMABLEONE\"," +
			"\"consumableType\":\"CHEMS\"," +
			"\"addictionType\":\"PSYCHO\"" +
			"}]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(consumableLoaderService, times(1)).loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
		verify(consumableMapper, times(1)).convertAllToDTO(anyList());
	}
}

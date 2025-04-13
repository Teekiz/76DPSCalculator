package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadouts.MutationController;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.MutationDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.MutationMapper;
import java.util.ArrayList;
import java.util.Collections;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MutationController.class)
public class MutationControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	MutationManager mutationManager;
	@MockBean
	MutationMapper mutationMapper;
	@MockBean
	DataLoaderService mutationLoaderService;

	String urlString = "/api/loadouts";

	@Test
	public void getMutations_WithMutationsInList() throws Exception
	{
		log.debug("{}Running test - getMutations_WithMutationsInList in MutationControllerTest.", System.lineSeparator());

		ExpressionParser parser = new SpelExpressionParser();
		Expression testExpression = parser.parseExpression("1 > 2");

		Mutation mutation = new Mutation("TEST1", "TESTMUTATION", "TESTING", null, null);
		MutationDTO mutationDTO = new MutationDTO("TEST1", "TESTMUTATION", "TESTING");

		List<MutationDTO> mutationDTOS = new ArrayList<>();
		mutationDTOS.add(mutationDTO);

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		loadout.getMutations().add(mutation);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(mutationMapper.convertAllToDTO(loadout.getMutations())).willReturn(mutationDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getMutations")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\":\"TEST1\"," +
			"\"name\":" + "\"TESTMUTATION\"," +
			"\"description\":" + "\"TESTING\""
			+ "}]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(mutationMapper, times(1)).convertAllToDTO(loadout.getMutations());
	}

	@Test
	public void getMutations_WithEmptyMutationsList() throws Exception
	{
		log.debug("{}Running test - getMutations_WithEmptyMutationsList in MutationControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(mutationMapper.convertAllToDTO(loadout.getMutations())).willReturn(new ArrayList<>());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getMutations")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(mutationMapper, times(1)).convertAllToDTO(loadout.getMutations());
	}

	@Test
	public void addMutation() throws Exception
	{
		log.debug("{}Running test - addMutation in MutationControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		given(loadoutManager.getLoadout(1)).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/addMutation")
					.param("loadoutID", "1")
					.param("mutationID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been added to your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(mutationManager, times(1)).addMutation("1", loadout);
	}

	@Test
	public void removeMutation() throws Exception
	{
		log.debug("{}Running test - removeMutation in MutationControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);


		given(loadoutManager.getLoadout(1)).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/removeMutation")
					.param("loadoutID", "1")
					.param("mutationID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been removed from your loadout."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(mutationManager, times(1)).removeMutation("1", loadout);
	}

	@Test
	public void getAvailableMutations() throws Exception
	{
		log.debug("{}Running test - getAvailableMutations in MutationControllerTest.", System.lineSeparator());

		Mutation mutationOne = new Mutation("TEST1", "TESTMUTATIONONE", "TESTING", null, null);
		Mutation mutationTwo = new Mutation("TEST2", "TESTMUTATIONTWO", "TESTING", null, null);

		MutationDTO mutationDTOne = new MutationDTO("TEST1", "TESTMUTATIONONE", "TESTING");
		MutationDTO mutationDTOTwo = new MutationDTO("TEST2", "TESTMUTATIONTWO", "TESTING");

		List<Mutation> availableMutations = new ArrayList<>();
		availableMutations.add(mutationOne);
		availableMutations.add(mutationTwo);

		List<MutationDTO> availableMutationsDTOs = new ArrayList<>();
		availableMutationsDTOs.add(mutationDTOne);
		availableMutationsDTOs.add(mutationDTOTwo);

		given(mutationLoaderService.loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(),
			ArgumentMatchers.any())).willReturn(Collections.singletonList(availableMutations));
		given(mutationMapper.convertAllToDTO(anyList())).willReturn(availableMutationsDTOs);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableMutations")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{" +
			"\"id\":\"TEST1\"," +
			"\"name\":\"TESTMUTATIONONE\"," +
			"\"description\":\"TESTING\"" +
			"},{" +
			"\"id\":\"TEST2\"," +
			"\"name\":\"TESTMUTATIONTWO\"," +
			"\"description\":\"TESTING\"" +
			"}]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(mutationLoaderService, times(1)).loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
		verify(mutationMapper, times(1)).convertAllToDTO(anyList());
	}
}

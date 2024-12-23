package Tekiz._DPSCalculator._DPSCalculator.controllers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers.LoadoutController;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.LoadoutDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.player.PlayerDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LoadoutMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(LoadoutController.class)
public class LoadoutControllerTest
{
	/**
	 * {\"loadoutID\":1,\"weapon\":null,\"perks\":{},\"consumables\":{},
	 * \"armour\":[],\"player\":{\"specials\":{\"strength\":1,\"perception\":1,
	 * \"endurance\":1,\"charisma\":1,\"intelligence\":1,\"agility\":1,\"luck\":1,\"minSpecialValue\":1,
	 * \"maxSpecialValue\":15},\"maxHP\":250.0,\"currentHP\":250.0,\"level\":1,\"isAiming\":false,\"isSneaking\":false},
	 * \"environment\":{},\"mutations\":[]}"
	 */
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutMapper loadoutMapper;
	@MockBean
	LoadoutManager loadoutManager;

	String urlString = "/api/loadouts";

	@Test
	public void retrieveLoadoutTest() throws Exception
	{
		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(), new Player(), null, new HashSet<>());
		LoadoutDTO mockDTO = new LoadoutDTO(1, null, new ArrayList<>(), new PlayerDTO(), new ArrayList<>(), new ArrayList<>());

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(mockLoadout);
		given(loadoutMapper.convertLoadoutToLoadoutDTO(mockLoadout)).willReturn(mockDTO);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getLoadout")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "{\"loadoutID\":1,\"weapon\":null,\"perks\":[],\"player\":{\"maxHP\":0.0,\"currentHP\":0.0,\"specials\":null},\"consumables\":[],\"mutations\":[]}";
		assertThat(response.getContentAsString()).contains(expectedJson);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(loadoutMapper, times(1)).convertLoadoutToLoadoutDTO(mockLoadout);
	}

	@Test
	public void retrieveAllLoadoutsTest() throws Exception
	{
		Loadout mockLoadoutOne = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(), new Player(), null, new HashSet<>());
		Loadout mockLoadoutTwo = new Loadout(2, null, new HashMap<>(), new HashMap<>(), new HashSet<>(), new Player(), null, new HashSet<>());
		HashSet<Loadout> loadoutHashSet = new HashSet<>();
		loadoutHashSet.add(mockLoadoutOne);
		loadoutHashSet.add(mockLoadoutTwo);

		LoadoutDTO mockDTOOne = new LoadoutDTO(1, null, new ArrayList<>(), new PlayerDTO(), new ArrayList<>(), new ArrayList<>());
		LoadoutDTO mockDTOTwo = new LoadoutDTO(2, null, new ArrayList<>(), new PlayerDTO(), new ArrayList<>(), new ArrayList<>());
		List<LoadoutDTO> mockLoadoutList= new ArrayList<>();
		mockLoadoutList.add(mockDTOOne);
		mockLoadoutList.add(mockDTOTwo);

		given(loadoutManager.getLoadouts()).willReturn(loadoutHashSet);
		given(loadoutMapper.covertAllLoadoutsToDTOs(loadoutHashSet)).willReturn(mockLoadoutList);

		MockHttpServletResponse response = mockMvc.perform(
			MockMvcRequestBuilders.get(urlString + "/getLoadouts")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"loadoutID\":1,\"weapon\":null,\"perks\":[],\"player\":{\"maxHP\":0.0,\"currentHP\":0.0,\"specials\":null},\"consumables\":[],\"mutations\":[]}," +
			"{\"loadoutID\":2,\"weapon\":null,\"perks\":[],\"player\":{\"maxHP\":0.0,\"currentHP\":0.0,\"specials\":null},\"consumables\":[],\"mutations\":[]}]";
		assertThat(response.getContentAsString()).isEqualToIgnoringWhitespace(expectedJson);

		//verifying that the dependencies were used
		verify(loadoutManager, times(1)).getLoadouts();
		verify(loadoutMapper, times(1)).covertAllLoadoutsToDTOs(loadoutHashSet);
	}
}

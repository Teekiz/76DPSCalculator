package Tekiz._DPSCalculator._DPSCalculator.controllers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers.PlayerController;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PlayerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PlayerController.class)
public class PlayerControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	PlayerManager playerManager;
	@MockBean
	PlayerMapper playerMapper;

	String urlString = "/api/loadouts";

	@Test
	public void changeSpecial_WithValidLoadout() throws Exception
	{
		log.debug("{}Running test - changeSpecial_WithValidLoadout in PlayerControllerTest.", System.lineSeparator());

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/changeSpecial")
					.param("loadoutID", "1")
					.param("special", "STRENGTH")
					.param("value", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("STRENGTH has been changed to 1."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(playerManager, times(1)).setSpecial(mockLoadout, Specials.STRENGTH, 1);
	}

	//changeSpecialInvalidLoadout
	@Test
	public void changeSpecialInvalidLoadout() throws Exception
	{
		log.debug("{}Running test - changeSpecialInvalidLoadout in PlayerControllerTest.", System.lineSeparator());

		given(loadoutManager.getLoadout(1)).willReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/changeSpecial")
					.param("loadoutID", "1")
					.param("special", "STRENGTH")
					.param("value", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Cannot find loadout of ID 1"))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
	}

	@Test
	public void changeSpecials_WithValidLoadout() throws Exception
	{
		log.debug("{}Running test - changeSpecials_WithValidLoadout in PlayerControllerTest.", System.lineSeparator());

		SpecialDTO specialDTO = new SpecialDTO(1,1,1,1,1,1,1);
		ObjectMapper objectMapper = new ObjectMapper();
		String specialDTOJson = objectMapper.writeValueAsString(specialDTO);

		Loadout mockLoadout = new Loadout(1, null, new HashMap<>(), new HashMap<>(), new HashSet<>(),
			new Player(), null, new HashSet<>());

		given(loadoutManager.getLoadout(1)).willReturn(mockLoadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/changeSpecials")
					.param("loadoutID", "1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(specialDTOJson)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("1 has been changed."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(playerManager, times(1)).setSpecialsFromDTO(any(Loadout.class), any(SpecialDTO.class));
	}

	@Test
	public void changeSpecials_WithInvalidLoadout() throws Exception
	{
		log.debug("{}Running test - changeSpecials_WithInvalidLoadout in PlayerControllerTest.", System.lineSeparator());

		SpecialDTO specialDTO = new SpecialDTO(1,1,1,1,1,1,1);
		ObjectMapper objectMapper = new ObjectMapper();
		String specialDTOJson = objectMapper.writeValueAsString(specialDTO);

		given(loadoutManager.getLoadout(1)).willReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/changeSpecials")
					.param("loadoutID", "1")
					.contentType(MediaType.APPLICATION_JSON)
					.content(specialDTOJson)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Cannot find loadout of ID 1"))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
	}
}

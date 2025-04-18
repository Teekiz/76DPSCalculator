package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadouts.PlayerController;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PlayerMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

		Loadout mockLoadout = mock(Loadout.class);
		when(mockLoadout.getLoadoutID()).thenReturn(1);

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

	@Test
	public void changeSpecials_WithValidLoadout() throws Exception
	{
		log.debug("{}Running test - changeSpecials_WithValidLoadout in PlayerControllerTest.", System.lineSeparator());

		SpecialDTO specialDTO = new SpecialDTO(1,1,1,1,1,1,1);
		ObjectMapper objectMapper = new ObjectMapper();
		String specialDTOJson = objectMapper.writeValueAsString(specialDTO);

		Loadout mockLoadout = mock(Loadout.class);
		when(mockLoadout.getLoadoutID()).thenReturn(1);

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
}

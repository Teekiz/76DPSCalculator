package Tekiz._DPSCalculator._DPSCalculator.controllers.servicecontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.servicecontrollers.DamageCalculationController;
import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.DPSDetailsMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(DamageCalculationController.class)
public class WeaponDamageCalculationControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	DamageCalculationService damageCalculationService;
	@MockBean
	DPSDetailsMapper dpsDetailsMapper;

	String urlString = "/api/services";

	@Test
	public void calculateLoadout_WithValidLoadout() throws Exception
	{
		Loadout mockLoadout = mock(Loadout.class);
		when(mockLoadout.getLoadoutID()).thenReturn(1);
		DPSDetails dpsDetails = mock(DPSDetails.class);
		DPSDetailsDTO dpsDetailsDTO = mock(DPSDetailsDTO.class);
		when(dpsDetailsDTO.getTotalDamagePerShot()).thenReturn(2.0);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(mockLoadout);
		given(damageCalculationService.calculateOutgoingDamage(any(Loadout.class))).willReturn(dpsDetails);
		given(dpsDetailsMapper.convertToDTO(any(DPSDetails.class))).willReturn(dpsDetailsDTO);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getLoadoutDPS")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(damageCalculationService, times(1)).calculateOutgoingDamage(mockLoadout);

		String expectedJson = "2.0";
		assertThat(response.getContentAsString()).contains(expectedJson);
	}

	@Test
	public void calculateAllLoadouts() throws Exception
	{
		Loadout mockLoadoutOne  = mock(Loadout.class);
		when(mockLoadoutOne.getLoadoutID()).thenReturn(1);
		DPSDetails mockDpsDetailsOne = mock(DPSDetails.class);
		DPSDetailsDTO mockDpsDetailsDTOOne = mock(DPSDetailsDTO.class);
		given(mockDpsDetailsDTOOne.getTotalDamagePerShot()).willReturn(2.0);

		Loadout mockLoadoutTwo = mock(Loadout.class);
		when(mockLoadoutTwo.getLoadoutID()).thenReturn(2);
		DPSDetails mockDpsDetailsTwo = mock(DPSDetails.class);
		DPSDetailsDTO mockDpsDetailsDTOTwo = mock(DPSDetailsDTO.class);
		given(mockDpsDetailsDTOTwo.getTotalDamagePerShot()).willReturn(34.2);

		Set<Loadout> mockLoadouts = new HashSet<>();
		mockLoadouts.add(mockLoadoutOne);
		mockLoadouts.add(mockLoadoutTwo);

		List<DPSDetailsDTO> dpsDetailsDTOList = new ArrayList<>(Arrays.asList(mockDpsDetailsDTOOne, mockDpsDetailsDTOTwo)){};

		given(loadoutManager.getLoadouts()).willReturn(mockLoadouts);
		given(damageCalculationService.calculateOutgoingDamage(mockLoadoutOne)).willReturn(mockDpsDetailsOne);
		given(damageCalculationService.calculateOutgoingDamage(mockLoadoutTwo)).willReturn(mockDpsDetailsTwo);
		given(dpsDetailsMapper.convertAllToDTO(any())).willReturn(dpsDetailsDTOList);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAllLoadoutsDPS")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJsonOne = "34.2";
		String expectedJsonTwo = "2.0";

		assertThat(response.getContentAsString()).contains(expectedJsonOne);
		assertThat(response.getContentAsString()).contains(expectedJsonTwo);

		verify(loadoutManager, times(1)).getLoadouts();
		verify(damageCalculationService, times(2)).calculateOutgoingDamage(any());
	}

	@Test
	public void calculateAllLoadouts_WithNoAvailableLoadouts() throws Exception
	{
		given(loadoutManager.getLoadouts()).willReturn(new HashSet<>());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAllLoadoutsDPS")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[]";
		assertThat(response.getContentAsString()).contains(expectedJson);

		verify(loadoutManager, times(1)).getLoadouts();
		verify(damageCalculationService, times(0)).calculateOutgoingDamage(any());
	}
}

package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers.WeaponController;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.WeaponMapper;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
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
@WebMvcTest(WeaponController.class)
public class WeaponControllerTest
{
	@Autowired
	MockMvc mockMvc;
	@MockBean
	WeaponManager weaponManager;
	@MockBean
	WeaponMapper weaponMapper;
	@MockBean
	LoadoutManager loadoutManager;
	@MockBean
	DataLoaderService weaponLoaderService;
	@MockBean
	WeaponFactory weaponFactory;

	String urlString = "/api/loadouts";

	@Test
	public void getWeapon_WithValidID() throws Exception
	{
		log.debug("{}Running test - getWeapon_WithValidID in WeaponControllerTest.", System.lineSeparator());
		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.fireRate(10)
			.build();

		WeaponDetailsDTO weaponDetailsDTO = WeaponDetailsDTO.builder()
			.id("WEAPONTEST1")
			.name("TEST1")
			.weaponType("PISTOL")
			.build();

		Loadout loadout = mock(Loadout.class);
		when(loadout.getWeapon()).thenReturn(weapon);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);
		given(weaponMapper.convertToRangedOrMeleeDTO(weapon)).willReturn(weaponDetailsDTO);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeapon")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "{"
			+ "\"id\":\"WEAPONTEST1\","
			+ "\"name\":\"TEST1\","
			+ "\"weaponType\":\"PISTOL\","
			+ "\"weaponDamageByLevel\":null,"
			+ "\"apCost\":0"
			+ "}";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(weaponMapper, times(1)).convertToRangedOrMeleeDTO(weapon);
	}

	@Test
	public void getWeapon_WithNullWeapon() throws Exception
	{
		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		given(loadoutManager.getLoadout(ArgumentMatchers.anyInt())).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeapon")
					.param("loadoutID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		assertThat(response.getContentAsString().isEmpty());
		verify(loadoutManager, times(1)).getLoadout(1);
	}

	@Test
	public void setWeapon_WithValidWeaponID() throws Exception
	{
		log.debug("{}Running test - setWeapon_WithValidWeaponID in WeaponControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		given(loadoutManager.getLoadout(1)).willReturn(loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/setWeapon")
					.param("loadoutID", "1")
					.param("weaponID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string("Weapon has been updated."))
			.andReturn().getResponse();

		verify(loadoutManager, times(1)).getLoadout(1);
		verify(weaponManager, times(1)).setWeapon("1", loadout);
	}

	@Test
	public void getWeaponDetails_WithValidWeaponID() throws Exception
	{
		log.debug("{}Running test - getWeaponDetails_WithValidWeaponID in WeaponControllerTest.", System.lineSeparator());

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.fireRate(10)
			.build();

		WeaponDetailsDTO weaponDetailsDTO = WeaponDetailsDTO.builder()
			.id("WEAPONTEST1")
			.name("TEST1")
			.weaponType("PISTOL")
			.build();


		given(weaponLoaderService.loadData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(weapon);
		given(weaponMapper.convertToDetailsDTO(weapon)).willReturn(weaponDetailsDTO);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponDetails")
					.param("weaponID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "{"
			+ "\"id\":\"WEAPONTEST1\","
			+ "\"name\":\"TEST1\","
			+ "\"weaponType\":\"PISTOL\","
			+ "\"weaponDamageByLevel\":null,"
			+ "\"apCost\":0"
			+ "}";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(weaponLoaderService, times(1)).loadData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
		verify(weaponMapper, times(1)).convertToDetailsDTO(weapon);
	}

	@Test
	public void getWeaponDetails_WithInvalidWeaponID() throws Exception
	{
		log.debug("{}Running test - getWeaponDetails_WithInvalidWeaponID in WeaponControllerTest.", System.lineSeparator());

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponDetails")
					.param("weaponID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		assertThat(response.getContentAsString().isEmpty());
		verify(weaponLoaderService, times(1)).loadData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
	}

	@Test
	public void getAvailableWeapons() throws Exception
	{
		log.debug("{}Running test - getAvailableWeapons in WeaponControllerTest.", System.lineSeparator());
		Weapon weaponOne = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPONONE")
			.weaponType(WeaponType.PISTOL)
			.fireRate(20)
			.build();

		Weapon weaponTwo = RangedWeapon.builder()
			.id("TESTWEAPON2")
			.name("TESTWEAPONTWO")
			.weaponType(WeaponType.RIFLE)
			.fireRate(10)
			.build();

		WeaponNameDTO weaponDetailsDTOOne = new WeaponNameDTO("TESTWEAPON1", "TESTWEAPONONE");
		WeaponNameDTO weaponDetailsDTOTwo = new WeaponNameDTO("TESTWEAPON2", "TESTWEAPONTWO");

		List<Weapon> availableWeapons = new ArrayList<>();
		availableWeapons.add(weaponOne);
		availableWeapons.add(weaponTwo);

		List<WeaponNameDTO> availableWeaponsDTOs = new ArrayList<>();
		availableWeaponsDTOs.add(weaponDetailsDTOOne);
		availableWeaponsDTOs.add(weaponDetailsDTOTwo);

		given(weaponLoaderService.loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(Collections.singletonList(availableWeapons));
		given(weaponMapper.convertAllToNameDTO(anyList())).willReturn(availableWeaponsDTOs);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableWeapons")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "["
			+ "{\"id\":\"TESTWEAPON1\","
			+ "\"name\":\"TESTWEAPONONE\"},"
			+ "{\"id\":\"TESTWEAPON2\","
			+ "\"name\":\"TESTWEAPONTWO\"}]";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(weaponLoaderService, times(1)).loadAllData(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any());
		verify(weaponMapper, times(1)).convertAllToNameDTO(anyList());
	}
}

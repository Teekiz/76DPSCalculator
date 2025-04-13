package Tekiz._DPSCalculator._DPSCalculator.controllers.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.controller.loadouts.WeaponController;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponModNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ModifierMapper;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.WeaponMapper;
import java.util.ArrayList;
import java.util.Collections;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
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
	@SpyBean
	WeaponMapper weaponMapper;
	@SpyBean
	ModifierMapper modifierMapper;
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
			.andExpect(status().isOk())
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
	public void setWeapon_WithInvalidWeaponID() throws Exception
	{
		log.debug("{}Running test - setWeapon_WithInvalidWeaponID in WeaponControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		when(loadout.getLoadoutID()).thenReturn(1);

		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		doThrow(new ResourceNotFoundException("Weapon not found while setting weapon. Weapon ID: 1.")).when(weaponManager).setWeapon("1", loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.post(urlString + "/setWeapon")
					.param("loadoutID", "1")
					.param("weaponID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Weapon not found while setting weapon. Weapon ID: 1."))
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
			.andExpect(status().isOk())
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

	@Test
	public void modifyWeapon_WithValidWeapon_WithValidModID() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_WithValidWeapon_WithValidModID in WeaponControllerTest.", System.lineSeparator());

		HashMap<ModType, ModificationSlot<WeaponMod>> modSlot = new HashMap<>();
		modSlot.put(ModType.RECEIVER, new ModificationSlot<>(null, ModType.RECEIVER, true, Set.of("TESTRECEIVER")));

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.modifications(modSlot)
			.fireRate(10)
			.build();

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		when(loadout.getWeapon()).thenReturn(weapon);

		WeaponMod weaponMod = new WeaponMod("1", "TESTRECIVER", "TESTRECIVER", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, new HashMap<>());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
	}

	@Test
	public void modifyWeapon_WithNullWeapon_WithValidModID() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_WithNullWeapon_WithValidModID in WeaponControllerTest.", System.lineSeparator());

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);

		WeaponMod weaponMod = new WeaponMod("1", "TESTRECIVER", "TESTRECIVER", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, new HashMap<>());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		doThrow(new ResourceNotFoundException("Weapon or mod not found during weapon modification. Modification ID: 1.")).when(weaponManager).modifyWeapon("1", loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedResponse = "Weapon or mod not found during weapon modification. Modification ID: 1.";
		assertEquals(expectedResponse, response.getContentAsString());
		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
	}

	@Test
	public void modifyWeapon_WithValidWeapon_WithInvalidModID() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_WithValidWeapon_WithInvalidModID in WeaponControllerTest.", System.lineSeparator());

		HashMap<ModType, ModificationSlot<WeaponMod>> modSlot = new HashMap<>();
		modSlot.put(ModType.RECEIVER, new ModificationSlot<>(null, ModType.RECEIVER, true, Set.of("TESTRECEIVER")));

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.modifications(modSlot)
			.fireRate(10)
			.build();

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		when(loadout.getWeapon()).thenReturn(weapon);

		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(null);
		doThrow(new ResourceNotFoundException("Weapon or mod not found during weapon modification. Modification ID: 1.")).when(weaponManager).modifyWeapon("1", loadout);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();

		String expectedResponse = "Weapon or mod not found during weapon modification. Modification ID: 1.";
		assertEquals(expectedResponse, response.getContentAsString());
		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
	}

	@Test
	public void modifyWeapon_SlotCannotBeChanged() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_SlotCannotBeChanged in WeaponControllerTest.", System.lineSeparator());

		HashMap<ModType, ModificationSlot<WeaponMod>> modSlot = new HashMap<>();
		modSlot.put(ModType.RECEIVER, new ModificationSlot<>(null, ModType.RECEIVER, false, Set.of("TESTRECEIVER")));

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.modifications(modSlot)
			.fireRate(10)
			.build();

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		when(loadout.getWeapon()).thenReturn(weapon);

		WeaponMod weaponMod = new WeaponMod("1", "TESTRECIVER", "TESTRECIVER", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, new HashMap<>());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
		assertNull(weapon.getModifications().get(ModType.RECEIVER).getCurrentModification());
	}

	@Test
	public void modifyWeapon_ModSlotIsNull() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_SlotCannotBeChanged in WeaponControllerTest.", System.lineSeparator());

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.modifications(null)
			.fireRate(10)
			.build();

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		when(loadout.getWeapon()).thenReturn(weapon);

		WeaponMod weaponMod = new WeaponMod("1", "TESTRECIVER", "TESTRECIVER", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, new HashMap<>());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
		assertNull(weapon.getModifications());
	}

	@Test
	public void modifyWeapon_IncompatibleModType() throws Exception
	{
		log.debug("{}Running test - modifyWeapon_IncompatibleModType in WeaponControllerTest.", System.lineSeparator());

		HashMap<ModType, ModificationSlot<WeaponMod>> modSlot = new HashMap<>();
		modSlot.put(ModType.RECEIVER, new ModificationSlot<>(null, ModType.MATERIAL, true, Set.of("TESTRECEIVER")));

		Weapon weapon = RangedWeapon.builder()
			.id("TESTWEAPON1")
			.name("TESTWEAPON")
			.weaponType(WeaponType.PISTOL)
			.modifications(modSlot)
			.fireRate(10)
			.build();

		Loadout loadout = mock(Loadout.class);
		given(loadoutManager.getLoadout(1)).willReturn(loadout);
		when(loadout.getWeapon()).thenReturn(weapon);

		WeaponMod weaponMod = new WeaponMod("1", "TESTRECIVER", "TESTRECIVER", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, new HashMap<>());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/modifyWeapon")
					.param("loadoutID", "1")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn().getResponse();

		verify(weaponManager, times(1)).modifyWeapon(anyString(), any(Loadout.class));
		assertNull(weapon.getModifications().get(ModType.RECEIVER).getCurrentModification());
	}

	@Test
	public void getAvailableWeaponMods() throws Exception
	{
		log.debug("{}Running test - getAvailableWeaponMods  in WeaponControllerTest.", System.lineSeparator());
		Weapon weapon = mock(Weapon.class);
		given(weaponLoaderService.loadData("1", Weapon.class, weaponFactory)).willReturn(weapon);

		List<WeaponModNameDTO> weaponModNameDTOS = new ArrayList<>();
		weaponModNameDTOS.add(WeaponModNameDTO.builder().id("1").name("TEST1").build());
		weaponModNameDTOS.add(WeaponModNameDTO.builder().id("2").name("TEST2").build());
		given(weaponMapper.convertToWeaponModNameDTO(any(List.class))).willReturn(weaponModNameDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableWeaponMods")
					.param("weaponID", "1")
					.param("modType", "RECEIVER")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\" : \"1\", \"name\" : \"TEST1\"}," +
			"{\"id\" : \"2\", \"name\" : \"TEST2\"}]";
		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);

		verify(weaponManager, times(1)).getAvailableWeaponMods(any(Weapon.class), any(ModType.class));
	}

	@Test
	public void getAvailableWeaponMods_withUnspecifiedType() throws Exception
	{
		log.debug("{}Running test - getAvailableWeaponMods_withUnspecifiedType  in WeaponControllerTest.", System.lineSeparator());

		Weapon weapon = mock(Weapon.class);
		given(weaponLoaderService.loadData("1", Weapon.class, weaponFactory)).willReturn(weapon);

		List<WeaponModNameDTO> weaponModNameDTOS = new ArrayList<>();
		weaponModNameDTOS.add(WeaponModNameDTO.builder().id("1").name("TEST1").build());
		weaponModNameDTOS.add(WeaponModNameDTO.builder().id("2").name("TEST2").build());
		when(weaponMapper.convertToWeaponModNameDTO(any(List.class))).thenReturn(weaponModNameDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableWeaponMods")
					.param("weaponID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = "[{\"id\" : \"1\", \"name\" : \"TEST1\"}," +
			"{\"id\" : \"2\", \"name\" : \"TEST2\"}]";
		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
		verify(weaponManager, times(1)).getAvailableWeaponMods(weapon, null);
	}

	@Test
	public void getAvailableWeaponMods_WithEmptyList() throws Exception
	{
		log.debug("{}Running test - getAvailableWeaponMods_WithEmptyList  in WeaponControllerTest.", System.lineSeparator());

		Weapon weapon = mock(Weapon.class);
		given(weaponLoaderService.loadData("1", Weapon.class, weaponFactory)).willReturn(weapon);

		List<WeaponModNameDTO> weaponModNameDTOS = new ArrayList<>();
		given(weaponMapper.convertToWeaponModNameDTO(any(List.class))).willReturn(weaponModNameDTOS);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getAvailableWeaponMods")
					.param("weaponID", "1")
					.param("modType", "RECEIVER")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertThat(response.getContentAsString().isEmpty());
		verify(weaponManager, times(1)).getAvailableWeaponMods(any(Weapon.class), any(ModType.class));
	}

	@Test
	public void getWeaponModDetails() throws Exception
	{
		log.debug("{}Running test - getWeaponModDetails in WeaponControllerTest.", System.lineSeparator());

		HashMap<ModifierTypes, ModifierValue<?>> modifiers = new HashMap<>();
		modifiers.put(ModifierTypes.DAMAGE_ADDITIVE, new ModifierValue<>(ModifierTypes.DAMAGE_ADDITIVE, 0.2));
		modifiers.put(ModifierTypes.CRITICAL_CONSUMPTION, new ModifierValue<>(ModifierTypes.CRITICAL_CONSUMPTION, 55));

		WeaponMod weaponMod = new WeaponMod("1", "Test", "Test", ModType.RECEIVER, ModSubType.NOT_APPLICABLE,
			ModifierSource.WEAPON_MODIFICATION, null, modifiers);
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(weaponMod);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponModDetails")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		String expectedJson = """
			{
			  "id": "1",
			  "name": "Test",
			  "modType": "RECEIVER",
			  "modificationEffects": [
			    {
			      "type": "DAMAGE_ADDITIVE",
			      "value": 0.2,
			      "userDescription": "Bonus damage: +20%."
			    },
			    {
			      "type": "CRITICAL_CONSUMPTION",
			      "value": 55,
			      "userDescription": "Critical consumption: -55%."
			    }
			  ]
			}""";

		JSONAssert.assertEquals(expectedJson, response.getContentAsString(), false);
	}

	@Test
	public void getWeaponModDetails_InvalidModID() throws Exception
	{
		log.debug("{}Running test - getWeaponModDetails in WeaponControllerTest.", System.lineSeparator());
		given(weaponLoaderService.loadData("1", WeaponMod.class, null)).willReturn(null);

		MockHttpServletResponse response = mockMvc.perform(
				MockMvcRequestBuilders.get(urlString + "/getWeaponModDetails")
					.param("modID", "1")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertThat(response.getContentAsString().isEmpty());
	}
}

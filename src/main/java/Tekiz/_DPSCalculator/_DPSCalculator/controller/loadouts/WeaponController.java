package Tekiz._DPSCalculator._DPSCalculator.controller.loadouts;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponModDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponModNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.WeaponMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Loadout Weapon API", description = "A group of APIs for weapons")
public class WeaponController
{
	private final LoadoutManager loadoutManager;
	private final WeaponManager weaponManager;
	private final WeaponMapper weaponMapper;
	private final DataLoaderService weaponLoaderService;
	private final WeaponFactory weaponFactory;
	@Autowired
	public WeaponController(LoadoutManager loadoutManager, WeaponManager weaponManager, WeaponMapper weaponMapper, DataLoaderService weaponLoaderService, WeaponFactory weaponFactory)
	{
		this.weaponFactory = weaponFactory;
		log.info("Weapon controller created.");
		this.loadoutManager = loadoutManager;
		this.weaponManager = weaponManager;
		this.weaponMapper = weaponMapper;
		this.weaponLoaderService = weaponLoaderService;
	}

	@Operation(summary = "Get the weapon in the loadout.", description = "Retrieves the weapon in the loadout matching the ID.")
	@GetMapping("/getWeapon")
	public ResponseEntity<WeaponDetailsDTO> getWeapon(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		log.debug("Received request for /getWeapon for loadout ID {}. Found {}.", loadoutID, loadout.getWeapon());
		return ResponseEntity.ok(weaponMapper.convertToRangedOrMeleeDTO(loadout.getWeapon()));
	}
	@Operation(summary = "Set the weapon in the loadout.", description = "Set the weapon based on the weapon ID in the loadout matching the ID.")
	@PostMapping("/setWeapon")
	public ResponseEntity<String> setWeapon(@RequestParam int loadoutID, @RequestParam String weaponID) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		weaponManager.setWeapon(weaponID, loadout);
		return ResponseEntity.ok("Weapon has been updated.");
	}
	@Operation(summary = "Gets all available weapons names", description = "Retrieves a list of all weapon names that are available.")
	@GetMapping("/getAvailableWeapons")
	public ResponseEntity<List<WeaponNameDTO>> getAvailableWeapons() throws IOException
	{
		return ResponseEntity.ok(weaponMapper.convertAllToNameDTO(weaponLoaderService.loadAllData("Weapon", Weapon.class, weaponFactory)));
	}
	@Operation(summary = "Get all the details of a weapon", description = "Retrieves the weapon details of a weapon matching the weapon ID.")
	@GetMapping("/getWeaponDetails")
	public ResponseEntity<WeaponDetailsDTO> getWeaponDetails(@RequestParam String weaponID) throws IOException
	{
		Weapon weapon = weaponLoaderService.loadData(weaponID, Weapon.class, weaponFactory);
		WeaponDetailsDTO weaponDetails = weaponMapper.convertToDetailsDTO(weapon);
		log.debug("Request for weapon: {}. Returning: {}.", weaponID, weaponDetails);
		return ResponseEntity.ok(weaponDetails);
	}
	@Operation(summary = "Gets all available weapon mods.", description = "Retrieves a list of all weapon mod names that are available.")
	@GetMapping("/getAvailableWeaponMods")
	public ResponseEntity<List<WeaponModNameDTO>> getAvailableWeaponMods(@RequestParam String weaponID, @RequestParam(required = false) ModType modType) throws IOException, ResourceNotFoundException
	{
		Weapon weapon = weaponLoaderService.loadData(weaponID, Weapon.class, weaponFactory);
		List<WeaponMod> weaponMods = weaponManager.getAvailableWeaponMods(weapon, modType);
		return ResponseEntity.ok(weaponMapper.convertToWeaponModNameDTO(weaponMods));
	}
	@Operation(summary = "Gets a weapon mods details.", description = "Retrieves a detailed list of information about a weapon mod.")
	@GetMapping("/getWeaponModDetails")
	public ResponseEntity<WeaponModDTO> getWeaponModDetails(@RequestParam String modID) throws IOException
	{
		WeaponMod weaponMod = weaponLoaderService.loadData(modID, WeaponMod.class, null);
		return ResponseEntity.ok(weaponMapper.convertToWeaponModDTO(weaponMod));
	}
	@Operation(summary = "Modifies the current weapon.", description = "Modifies the current loadouts weapon with the provided modification ID.")
	@GetMapping("/modifyWeapon")
	public ResponseEntity<String> modifyWeapon(@RequestParam int loadoutID, @RequestParam String modID) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (weaponManager.modifyWeapon(modID, loadout)){
			log.debug("Request to modify weapon in loadout: {}. Modification ID: {}.", loadoutID, modID);
			return ResponseEntity.ok("Modification " + modID + " has been applied to weapon in loadout " + loadoutID + ".");
		}
		return ResponseEntity.badRequest().body("Weapon mod could not be applied.");
	}
}

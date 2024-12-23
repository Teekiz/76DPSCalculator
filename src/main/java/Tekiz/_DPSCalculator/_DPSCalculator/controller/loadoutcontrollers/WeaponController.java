package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.WeaponMapper;
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

	@GetMapping("/getWeapon")
	public ResponseEntity<WeaponDetailsDTO> getWeapon(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		log.debug("Received request for /getWeapon for loadout ID {}. Found {}.", loadoutID, loadout.getWeapon());
		if (loadout.getWeapon() == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(weaponMapper.convertToRangedOrMeleeDTO(loadout.getWeapon()));
	}
	@PostMapping("/setWeapon")
	public ResponseEntity<String> setWeapon(@RequestParam int loadoutID, @RequestParam String weaponID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		weaponManager.setWeapon(weaponID, loadout);
		return ResponseEntity.ok("Weapon has been updated.");
	}
	@GetMapping("/getAvailableWeapons")
	public ResponseEntity<List<WeaponNameDTO>> getAvailableWeapons() throws IOException
	{
		return ResponseEntity.ok(weaponMapper.convertAllToNameDTO(weaponLoaderService.loadAllData("Weapon", Weapon.class, weaponFactory)));
	}
	@GetMapping("/getWeaponDetails")
	public ResponseEntity<WeaponDetailsDTO> getWeaponDetails(@RequestParam String weaponID) throws IOException
	{
		Weapon weapon = weaponLoaderService.loadData(weaponID, Weapon.class, weaponFactory);
		if (weapon == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		WeaponDetailsDTO weaponDetails = weaponMapper.convertToDetailsDTO(weapon);
		log.debug("Request for weapon: {}. Returning: {}.", weaponID, weaponDetails);
		return ResponseEntity.ok(weaponDetails);
	}
}

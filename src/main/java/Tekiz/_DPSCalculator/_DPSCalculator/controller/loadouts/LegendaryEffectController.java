package Tekiz._DPSCalculator._DPSCalculator.controller.loadouts;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LegendaryEffectManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LegendaryEffectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Legendary Effect API", description = "A group of APIs for legendary effects")
public class LegendaryEffectController
{
	private final LoadoutManager loadoutManager;
	private final LegendaryEffectManager legendaryEffectManager;
	private final LegendaryEffectMapper legendaryEffectMapper;

	@Autowired
	public LegendaryEffectController(LoadoutManager loadoutManager, LegendaryEffectManager legendaryEffectManager, LegendaryEffectMapper legendaryEffectMapper)
	{
		log.info("Legendary effect controller created.");
		this.loadoutManager = loadoutManager;
		this.legendaryEffectManager = legendaryEffectManager;
		this.legendaryEffectMapper = legendaryEffectMapper;
	}

	@Operation(summary = "Gets all available legendary effects, filtered by star and category if applicable", description = "Retrieves a list of all legendary effects that are available.")
	@GetMapping("/getAvailableLegendaryEffects")
	public ResponseEntity<List<LegendaryEffectDTO>> getAvailableLegendaryEffects(@RequestParam(required = false) StarType starType, @RequestParam(required = false) Category category) throws IOException
	{
		List<LegendaryEffect> legendaryEffects = legendaryEffectManager.getAvailableLegendaryEffects(starType, category);
		List<LegendaryEffectDTO> legendaryEffectDTOs = legendaryEffectMapper.convertAllToDTO(legendaryEffects);
		return ResponseEntity.ok(legendaryEffectDTOs);
	}

	@Operation(summary = "Gets all legendary effects on the current weapon", description = "Retrieves a list of all legendary effects that are currently applied to the current weapon.")
	@GetMapping("/getWeaponLegendaryEffects")
	public ResponseEntity<List<LegendaryEffectDTO>> getWeaponLegendaryEffects(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		Weapon weapon = loadout.getWeapon();
		if (weapon == null || weapon.getLegendaryEffects() == null){
			log.warn("Cannot get legendary effects on weapon.");
			return ResponseEntity.notFound().build();
		}
		List<LegendaryEffect> legendaryEffects = weapon.getLegendaryEffects().getAllEffects();
		List<LegendaryEffectDTO> legendaryEffectDTOs = legendaryEffectMapper.convertAllToDTO(legendaryEffects);
		return ResponseEntity.ok(legendaryEffectDTOs);
	}

	@Operation(summary = "Gets all legendary effects on the armour piece matching the slot", description = "Retrieves a list of all legendary effects that are currently applied to the matching armour slot.")
	@GetMapping("/getArmourLegendaryEffects")
	public ResponseEntity<List<LegendaryEffectDTO>> getArmourLegendaryEffects(@RequestParam int loadoutID, @RequestParam ArmourType armourType, @RequestParam ArmourSlot armourSlot)
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		if (armour == null || armour.getLegendaryEffects() == null){
			log.warn("Cannot get legendary effects on armour.");
			return ResponseEntity.notFound().build();
		}
		List<LegendaryEffect> legendaryEffects = armour.getLegendaryEffects().getAllEffects();
		List<LegendaryEffectDTO> legendaryEffectDTOs = legendaryEffectMapper.convertAllToDTO(legendaryEffects);
		return ResponseEntity.ok(legendaryEffectDTOs);
	}

	@Operation(summary = "Adds a legendary effect onto the current weapon", description = "Adds a legendary effect onto the weapon in the provided loadout.")
	@PostMapping("/addWeaponLegendaryEffect")
	public ResponseEntity<String> addWeaponLegendaryEffect(@RequestParam int loadoutID, @RequestParam String legendaryEffectID) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (legendaryEffectManager.addLegendaryEffect(legendaryEffectID, loadout.getWeapon(), loadout)){
			log.debug("Request to add legendary effect to weapon in loadout: {}. Legendary effect ID: {}.", loadoutID, legendaryEffectID);
			return ResponseEntity.ok("Legendary effect " + legendaryEffectID + " has been applied to weapon in loadout " + loadoutID + ".");
		}
		return ResponseEntity.badRequest().body("Weapon legendary effect could not be applied.");
	}

	@Operation(summary = "Removes a legendary effect on the current weapon", description = "Removes a legendary effect from the weapon in the provided loadout.")
	@PostMapping("/removeWeaponLegendaryEffect")
	public ResponseEntity<String> removeWeaponLegendaryEffect(@RequestParam int loadoutID, @RequestParam StarType starType) throws ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (legendaryEffectManager.removeLegendaryEffect(starType, loadout.getWeapon(), loadout)){
			log.debug("Request to remove legendary effect from weapon in loadout: {}. Legendary effect star: {}.", loadoutID, starType);
			return ResponseEntity.ok(starType + " legendary effect has been removed from weapon in loadout " + loadoutID + ".");
		}
		return ResponseEntity.badRequest().body("Weapon legendary effect could not be removed.");
	}

	@Operation(summary = "Adds a legendary effect onto the matching armour piece.", description = "Adds a legendary effect onto the armour piece in the provided loadout.")
	@PostMapping("/addArmourLegendaryEffect")
	public ResponseEntity<String> addArmourLegendaryEffect(@RequestParam int loadoutID, @RequestParam String legendaryEffectID, @RequestParam ArmourType armourType, @RequestParam ArmourSlot armourSlot) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		if (legendaryEffectManager.addLegendaryEffect(legendaryEffectID, armour, loadout)){
			log.debug("Request to add legendary effect to armour armour in loadout: {}. Legendary effect ID: {}. Armour slot: {}. ArmourType: {}.", loadoutID, legendaryEffectID, armourType, armourSlot);
			return ResponseEntity.ok("Legendary effect " + legendaryEffectID + " has been applied to " + armourType + " "+ armourSlot + " in loadout " + loadoutID + ".");
		}
		return ResponseEntity.badRequest().body("Armour legendary effect could not be applied.");
	}

	@Operation(summary = "Removes a legendary effect from the matching armour piece", description = "Removes a legendary effect from the armour in the provided loadout.")
	@PostMapping("/removeArmourLegendaryEffect")
	public ResponseEntity<String> removeArmourLegendaryEffect(@RequestParam int loadoutID, @RequestParam StarType starType, @RequestParam ArmourType armourType, @RequestParam ArmourSlot armourSlot) throws ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		if (legendaryEffectManager.removeLegendaryEffect(starType,armour, loadout)){
			log.debug("Request to remove legendary effect from {} {} in loadout: {}. Legendary effect star: {}.", armourType, armourSlot, loadoutID, starType);
			return ResponseEntity.ok(starType + " legendary effect has been removed from " + armourType + " "+ armourSlot + " in loadout " + loadoutID + ".");}
		return ResponseEntity.badRequest().body("Armour legendary effect could not be removed.");
	}
}

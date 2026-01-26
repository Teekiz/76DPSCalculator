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
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LegendaryEffectManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LegendaryEffectMapper;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.WeaponMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static Tekiz._DPSCalculator._DPSCalculator.controller.util.ControllerUtility.sanitizeString;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Legendary Effect API", description = "A group of APIs for legendary effects")
public class LegendaryEffectController
{
	private final LoadoutManager loadoutManager;
	private final LegendaryEffectManager legendaryEffectManager;
	private final LegendaryEffectMapper legendaryEffectMapper;
	private final WeaponMapper weaponMapper;

	@Autowired
	public LegendaryEffectController(LoadoutManager loadoutManager, LegendaryEffectManager legendaryEffectManager, LegendaryEffectMapper legendaryEffectMapper, WeaponMapper weaponMapper)
	{
		this.weaponMapper = weaponMapper;
		log.info("Legendary effect controller created.");
		this.loadoutManager = loadoutManager;
		this.legendaryEffectManager = legendaryEffectManager;
		this.legendaryEffectMapper = legendaryEffectMapper;
	}

	@Operation(summary = "Gets all available legendary effects, filtered by star and category if applicable", description = "Retrieves a list of all legendary effects that are available.")
	@GetMapping("/getAvailableLegendaryEffects")
	public ResponseEntity<List<LegendaryEffectDTO>> getAvailableLegendaryEffects(@RequestParam(required = false) StarType starType, @RequestParam(required = false) Category category) throws IOException
	{
		log.debug("Received request for getAvailableLegendaryEffects, starType: {}, category: {}", starType, category);
		List<LegendaryEffect> legendaryEffects = legendaryEffectManager.getAvailableLegendaryEffects(starType, category);
		List<LegendaryEffectDTO> legendaryEffectDTOs = legendaryEffectMapper.convertAllToDTO(legendaryEffects);
		return ResponseEntity.ok(legendaryEffectDTOs);
	}

	@Operation(summary = "Changes a legendary effect on the current weapon", description = "Changes a legendary effect on the weapon in the provided loadout.")
	@PatchMapping("/changeWeaponLegendaryEffect")
	public ResponseEntity<WeaponDetailsDTO> changeWeaponLegendaryEffect(@RequestParam int loadoutID, @RequestParam StarType starType, @RequestParam String legendaryEffectID) throws IOException, ResourceNotFoundException
	{
		log.debug("Request to add legendary effect to weapon in loadout: {}. Legendary effect ID: {}.", loadoutID, legendaryEffectID);
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		legendaryEffectManager.changeLegendaryEffect(legendaryEffectID, starType, loadout.getWeapon(), loadout);
		return ResponseEntity.ok(weaponMapper.convertToRangedOrMeleeDTO(loadout.getWeapon()));
	}

	@Operation(summary = "Changes a legendary effect onto the matching armour piece.", description = "Changes a legendary effect onto the armour piece in the provided loadout.")
	@PatchMapping("/changeArmourLegendaryEffect")
	public ResponseEntity<String> changeArmourLegendaryEffect(@RequestParam int loadoutID, @RequestParam StarType starType, @RequestParam String legendaryEffectID, @RequestParam ArmourType armourType, @RequestParam ArmourSlot armourSlot) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		legendaryEffectManager.changeLegendaryEffect(legendaryEffectID, starType, armour, loadout);
		log.debug("Request to add legendary effect to armour armour in loadout: {}. Legendary effect ID: {}. Armour slot: {}. ArmourType: {}.", loadoutID, legendaryEffectID, armourType, armourSlot);
		return ResponseEntity.ok("Legendary effect " + sanitizeString(legendaryEffectID) + " has been applied to " + sanitizeString(armourType) + " "+ sanitizeString(armourSlot) + " in loadout " + loadoutID + ".");
	}
}

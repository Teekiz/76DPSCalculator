package Tekiz._DPSCalculator._DPSCalculator.controller.loadouts;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourModDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourModNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.dto.ArmourNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ArmourFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ArmourManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ArmourMapper;
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

import static Tekiz._DPSCalculator._DPSCalculator.controller.util.ControllerUtility.sanitizeString;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Loadout Armour API", description = "A group of APIs for armour")
public class ArmourController
{
	private final LoadoutManager loadoutManager;
	private final ArmourManager armourManager;
	private final ArmourMapper armourMapper;
	private final DataLoaderService armourLoaderService;
	private final ArmourFactory armourFactory;
	@Autowired
	public ArmourController(LoadoutManager loadoutManager, ArmourManager armourManager, ArmourMapper armourMapper, DataLoaderService armourLoaderService, ArmourFactory armourFactory)
	{
		log.info("Armour controller created.");
		this.armourFactory = armourFactory;
		this.loadoutManager = loadoutManager;
		this.armourManager = armourManager;
		this.armourMapper = armourMapper;
		this.armourLoaderService = armourLoaderService;
	}

	@Operation(summary = "Get the armour in the loadout.", description = "Retrieves the armour in the loadout matching the ID.")
	@GetMapping("/getArmour")
	public ResponseEntity<List<ArmourDTO>> getArmour(@RequestParam int loadoutID)
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout.getArmour() != null){
			log.debug("Received request for /getArmour for loadout ID {}. Found {}.", loadoutID, loadout.getArmour());
			return ResponseEntity.ok(armourMapper.convertEquippedArmour(loadout.getArmour()));
		}
		return ResponseEntity.notFound().build();
	}
	@Operation(summary = "Adds a piece of armour in the loadout.", description = "Adds a piece of armour based on the armour ID in the loadout matching the ID.")
	@PostMapping("/addArmour")
	public ResponseEntity<String> addArmour(@RequestParam int loadoutID, @RequestParam String armourID, @RequestParam ArmourSlot armourSlot) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (armourManager.addArmour(armourID, armourSlot, loadout)){
			return ResponseEntity.ok(sanitizeString(armourID) + " has been added to slot " + sanitizeString(armourSlot) + ".");
		}
		return ResponseEntity.badRequest().body("Armour could not be applied.");
	}
	@Operation(summary = "Gets all available armour.", description = "Retrieves a list of all armour names that are available. Filters by type and slot if not null")
	@GetMapping("/getAvailableArmour")
	public ResponseEntity<List<ArmourNameDTO>> getAvailableArmour(@RequestParam(required = false) ArmourType armourType, @RequestParam(required = false) ArmourPiece armourPiece) throws IOException
	{
		return ResponseEntity.ok(armourMapper.convertAllToNameDTO(armourManager.getAvailableArmour(armourType, armourPiece)));
	}
	@Operation(summary = "Gets the details of the specific armour piece.", description = "Retrieves the armour details of a armour matching the armour ID.")
	@GetMapping("/getArmourDetails")
	public ResponseEntity<ArmourDTO> getArmourDetails(@RequestParam String armourID) throws IOException
	{
		Armour armour = armourLoaderService.loadData(armourID, Armour.class, armourFactory);
		ArmourDTO armourDetails = armourMapper.convertToDetailsDTO(armour);
		log.debug("Request for armour: {}. Returning: {}.", armourID, armourDetails);
		return ResponseEntity.ok(armourDetails);
	}
	@Operation(summary = "Gets all available armour mods.", description = "Retrieves a list of all armour mod names that are available. Filtered by the type and slot.")
	@GetMapping("/getAvailableArmourMods")
	public ResponseEntity<List<ArmourModNameDTO>> getAvailableArmourMods(@RequestParam String armourID, @RequestParam(required = false) ModType modType) throws IOException, ResourceNotFoundException
	{
		Armour armour = armourLoaderService.loadData(armourID, Armour.class, armourFactory);
		List<ArmourMod> armourMods = armourManager.getAvailableArmourMods(armour, modType);
		return ResponseEntity.ok(armourMapper.convertToArmourModNameDTO(armourMods));
	}
	@Operation(summary = "Gets an armour mods details.", description = "Retrieves a detailed list of information about an armour mod.")
	@GetMapping("/getArmourModDetails")
	public ResponseEntity<ArmourModDTO> getArmourModDetails(@RequestParam String modID) throws IOException
	{
		ArmourMod armourMod = armourLoaderService.loadData(modID, ArmourMod.class, null);
		ArmourModDTO armourModDTO = armourMapper.convertToArmourModDTO(armourMod);
		return ResponseEntity.ok(armourModDTO);
	}
	@Operation(summary = "Modifies the current armour.", description = "Modifies the current loadouts armour with the provided modification ID.")
	@PostMapping("/modifyArmour")
	public ResponseEntity<String> modifyArmour(@RequestParam int loadoutID, @RequestParam String modID, @RequestParam ArmourType armourType, @RequestParam ArmourSlot armourSlot) throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (armourManager.modifyArmour(modID, armourType, armourSlot, loadout)){
			log.debug("Request to modify armour in loadout: {}. Modification ID: {}. Armour slot: {}. ArmourType: {}.", loadoutID, modID, armourType, armourSlot);
			return ResponseEntity.ok("Modification " + sanitizeString(modID) + " has been applied to " + sanitizeString(armourType) + " "+ sanitizeString(armourSlot) + " in loadout " + loadoutID + ".");
		}
		return ResponseEntity.badRequest().body("Armour mod could not be applied.");
	}
}

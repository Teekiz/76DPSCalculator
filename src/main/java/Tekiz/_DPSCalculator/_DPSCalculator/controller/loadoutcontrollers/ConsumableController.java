package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.ConsumableDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.ConsumableMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Collections;
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
@Tag(name = "Loadout Consumable API", description = "A group of APIs for consumables")
public class ConsumableController
{
	private final LoadoutManager loadoutManager;
	private final ConsumableManager consumableManager;
	private final ConsumableMapper consumableMapper;
	private final DataLoaderService consumableLoaderService;

	@Autowired
	public ConsumableController(LoadoutManager loadoutManager, ConsumableManager consumableManager,
								ConsumableMapper consumableMapper, DataLoaderService consumableLoaderService)
	{
		log.info("Consumable controller created.");
		this.loadoutManager = loadoutManager;
		this.consumableManager = consumableManager;
		this.consumableMapper = consumableMapper;
		this.consumableLoaderService = consumableLoaderService;
	}

	@Operation(summary = "Get consumables in loadout.", description = "Retrieves all consumables within a loadout provided by the loadoutID")
	@GetMapping("/getConsumables")
	public ResponseEntity<List<ConsumableDTO>> getConsumables(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout.getPerks() == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.EMPTY_LIST);
		}
		return ResponseEntity.ok(consumableMapper.convertAllToDTO(loadout.getConsumables()));
	}

	@Operation(summary = "Add a consumable to a loadout.", description = "Adds a consumable to the provided loadoutID using the consumable ID")
	@PostMapping("/addConsumable")
	public ResponseEntity<String> addConsumable(@RequestParam int loadoutID, @RequestParam String consumableID) throws IOException
	{
		log.debug("Add consumable called for consumable: {}.", consumableID);
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		consumableManager.addConsumable(consumableID, loadout);
		return ResponseEntity.ok(consumableID + " has been added to your loadout.");
	}

	@Operation(summary = "Removes a consumable from a loadout.", description = "Removes a consumable from the provided loadoutID using the consumable ID")
	@PostMapping("/removeConsumable")
	public ResponseEntity<String> removeConsumable(@RequestParam int loadoutID, @RequestParam String consumableID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		consumableManager.removeConsumable(consumableID, loadout);
		return ResponseEntity.ok(consumableID + " has been removed from your loadout.");
	}

	@Operation(summary = "Gets all available consumables.", description = "Retrieves a list of all available consumables.")
	@GetMapping("/getAvailableConsumables")
	public ResponseEntity<List<ConsumableDTO>> getAvailableConsumables() throws IOException
	{
		return ResponseEntity.ok(consumableMapper.convertAllToDTO(consumableLoaderService.loadAllData("consumables", Consumable.class, null)));
	}
}

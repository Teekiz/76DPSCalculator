package Tekiz._DPSCalculator._DPSCalculator.controller.loadouts;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.LoadoutDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LoadoutMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Loadout API", description = "A group of APIs for loadouts")
public class LoadoutController
{
	private final LoadoutManager loadoutManager;
	private final LoadoutMapper loadoutMapper;

	@Autowired
	public LoadoutController(LoadoutManager loadoutManager, LoadoutMapper loadoutMapper)
	{
		this.loadoutManager = loadoutManager;
		this.loadoutMapper = loadoutMapper;
	}

	@Operation(summary = "Gets a loadout", description = "Retrieves a loadout based on the loadoutID")
	@GetMapping("/getLoadout")
	public ResponseEntity<LoadoutDTO> getLoadout(@RequestParam int loadoutID) throws IOException
	{
		log.debug("/getLoadout has been called.");
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		return ResponseEntity.ok(loadoutMapper.convertLoadoutToLoadoutDTO(loadout));
	}

	@Operation(summary = "Gets all loadouts.", description = "Retrieves all loadout based on the session ID")
	@GetMapping("/getLoadouts")
	public ResponseEntity<List<LoadoutDTO>> getLoadouts()
	{
		log.debug("/getLoadouts called.");
		return ResponseEntity.ok(loadoutMapper.covertAllLoadoutsToDTOs(loadoutManager.getLoadouts()));
	}
}

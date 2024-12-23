package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.LoadoutDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.LoadoutMapper;
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

	@GetMapping("/getLoadout")
	public ResponseEntity<LoadoutDTO> getLoadout(@RequestParam int loadoutID) throws IOException
	{
		log.debug("/getLoadout has been called.");
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.ok(loadoutMapper.convertLoadoutToLoadoutDTO(loadout));
	}

	@GetMapping("/getLoadouts")
	public ResponseEntity<List<LoadoutDTO>> getLoadouts() throws IOException
	{
		log.debug("/getLoadouts called.");
		return ResponseEntity.ok(loadoutMapper.covertAllLoadoutsToDTOs(loadoutManager.getLoadouts()));
	}
}

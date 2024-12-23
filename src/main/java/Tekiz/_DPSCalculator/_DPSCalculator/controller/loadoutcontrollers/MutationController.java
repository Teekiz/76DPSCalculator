package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.MutationDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.MutationMapper;
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
public class MutationController
{
	private final LoadoutManager loadoutManager;
	private final MutationManager mutationManager;
	private final MutationMapper mutationMapper;
	private final DataLoaderService mutationLoaderService;

	@Autowired
	public MutationController(LoadoutManager loadoutManager, MutationManager mutationManager, MutationMapper mutationMapper, DataLoaderService mutationLoaderService)
	{
		log.info("Consumable controller created.");
		this.mutationManager = mutationManager;
		this.mutationMapper = mutationMapper;
		this.mutationLoaderService = mutationLoaderService;
		this.loadoutManager = loadoutManager;
	}

	@GetMapping("/getMutations")
	public ResponseEntity<List<MutationDTO>> getMutations(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout.getPerks() == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.EMPTY_LIST);
		}
		return ResponseEntity.ok(mutationMapper.convertAllToDTO(loadout.getMutations()));
	}

	@PostMapping("/addMutation")
	public ResponseEntity<String> addMutation(@RequestParam int loadoutID, @RequestParam String mutationID) throws IOException
	{
		log.debug("Add mutation called for mutation: {}.", mutationID);
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		mutationManager.addMutation(mutationID, loadout);
		return ResponseEntity.ok(mutationID + " has been added to your loadout.");
	}

	@PostMapping("/removeMutation")
	public ResponseEntity<String> removeMutation(@RequestParam int loadoutID, @RequestParam String mutationID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		mutationManager.removeMutation(mutationID, loadout);
		return ResponseEntity.ok(mutationID + " has been removed from your loadout.");
	}

	@GetMapping("/getAvailableMutations")
	public ResponseEntity<List<MutationDTO>> getAvailableMutations() throws IOException
	{
		return ResponseEntity.ok(mutationMapper.convertAllToDTO(mutationLoaderService.loadAllData("mutations", Mutation.class, null)));
	}
}

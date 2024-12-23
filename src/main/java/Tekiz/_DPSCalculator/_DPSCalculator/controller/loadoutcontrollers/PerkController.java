package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PerkMapper;
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
public class PerkController
{
	private final LoadoutManager loadoutManager;
	private final PerkManager perkManager;
	private final PerkMapper perkMapper;
	private final DataLoaderService perkLoaderService;
	@Autowired
	public PerkController(LoadoutManager loadoutManager, PerkManager perkManager, PerkMapper perkMapper, DataLoaderService perkLoaderService)
	{
		log.info("Perk controller created.");
		this.loadoutManager = loadoutManager;
		this.perkManager = perkManager;
		this.perkMapper = perkMapper;
		this.perkLoaderService = perkLoaderService;
	}

	@GetMapping("/getPerks")
	public ResponseEntity<List<PerkDTO>> getPerks(@RequestParam int loadoutID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout.getPerks() == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.EMPTY_LIST);
		}
		return ResponseEntity.ok(perkMapper.convertAllToDTO(loadout.getPerks()));
	}

	//todo - handle exceptions and if perk cannot be found
	@PostMapping("/addPerk")
	public ResponseEntity<String> addPerk(@RequestParam int loadoutID, @RequestParam String perkID) throws IOException
	{
		log.debug("Add perk called for perk: {}.", perkID);
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		perkManager.addPerk(perkID, loadout);
		return ResponseEntity.ok(perkID + " has been added to your loadout.");
	}

	@PostMapping("/removePerk")
	public ResponseEntity<String> removePerk(@RequestParam int loadoutID, @RequestParam String perkID) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		perkManager.removePerk(perkID, loadout);
		return ResponseEntity.ok(perkID + " has been removed from your loadout.");
	}

	@GetMapping("/getAvailablePerks")
	public ResponseEntity<List<PerkDTO>> getAvailablePerks() throws IOException
	{
		return ResponseEntity.ok(perkMapper.convertAllToDTO(perkLoaderService.loadAllData("perks", Perk.class, null)));
	}

	@PostMapping("/changePerkRank")
	public ResponseEntity<String> changePerkRank(@RequestParam int loadoutID, @RequestParam String perkID, @RequestParam int perkRank) throws IOException
	{
		log.debug("Received request to change perk rank for loadout {}. Perk name: {}, new rank: {}.", loadoutID, perkID, perkRank);
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		perkManager.changePerkRank(perkID, perkRank, loadout);
		return ResponseEntity.ok(perkID + "'s rank has been modified.");
	}
}

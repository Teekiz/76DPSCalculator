package Tekiz._DPSCalculator._DPSCalculator.controller;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PerkMapper;
import java.io.IOException;
import java.util.Collections;
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
@RequestMapping("/api")
public class PerkController
{
	private final LoadoutManager loadoutManager;
	private final PerkManager perkManager;
	private final PerkMapper perkMapper;
	@Autowired
	public PerkController(LoadoutManager loadoutManager, PerkManager perkManager, PerkMapper perkMapper)
	{
		log.info("Perk controller created.");
		this.loadoutManager = loadoutManager;
		this.perkManager = perkManager;
		this.perkMapper = perkMapper;
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
	@GetMapping("/addPerk")
	public ResponseEntity<String> addPerk(@RequestParam int loadoutID, @RequestParam String perkName) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		perkManager.addPerk(perkName, loadout);
		return ResponseEntity.ok(perkName + " has been added to your loadout.");
	}
}

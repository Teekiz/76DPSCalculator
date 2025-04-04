package Tekiz._DPSCalculator._DPSCalculator.controller.loadoutcontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.PlayerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/loadouts")
@Tag(name = "Loadout Player API", description = "A group of APIs for players")
public class PlayerController
{
	private final LoadoutManager loadoutManager;
	private final PlayerManager playerManager;
	private final PlayerMapper playerMapper;

	@Autowired
	public PlayerController(LoadoutManager loadoutManager, PlayerManager playerManager, PlayerMapper playerMapper)
	{
		this.loadoutManager = loadoutManager;
		this.playerManager = playerManager;
		this.playerMapper = playerMapper;
	}

	@Operation(summary = "Changes a single special attribute.", description = "Changes a single special attribute to the provided value in a loadout with the matching loadout ID.")
	@PostMapping("/changeSpecial")
	public ResponseEntity<String> changeSpecial(@RequestParam int loadoutID, String special, int value) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		if (loadout != null){
			playerManager.setSpecial(loadout, Specials.valueOf(special), value);
			return ResponseEntity.status(HttpStatus.OK).body(special + " has been changed to " + value + ".");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find loadout of ID " + loadoutID);
	}

	@Operation(summary = "Changes all special attributes.", description = "Changes all special attributes in a loadout with the matching loadout ID.")
	@PostMapping("/changeSpecials")
	public ResponseEntity<String> changeSpecials(@RequestParam int loadoutID, @RequestBody SpecialDTO specials) throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(loadoutID);
		log.debug("/changeSpecialsCalled for loadout {}. Specials: {}.", loadoutID, specials);
		if (loadout != null){

			playerManager.setSpecialsFromDTO(loadout, specials);
			return ResponseEntity.status(HttpStatus.OK).body(loadoutID + " has been changed.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot find loadout of ID " + loadoutID);
	}
}

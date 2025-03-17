package Tekiz._DPSCalculator._DPSCalculator.controller.servicecontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetails;
import Tekiz._DPSCalculator._DPSCalculator.model.calculations.DPSDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.mappers.DPSDetailsMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/services")
@Tag(name = "WeaponDamage Calculation Service API", description = "A group of APIs for calculating loadout damage.")
public class DamageCalculationController
{
	private final LoadoutManager loadoutManager;
	private final DamageCalculationService damageCalculationService;
	private final DPSDetailsMapper dpsDetailsMapper;


	@Autowired
	public DamageCalculationController(LoadoutManager loadoutManager, DamageCalculationService damageCalculationService, DPSDetailsMapper dpsDetailsMapper)
	{
		this.loadoutManager = loadoutManager;
		this.damageCalculationService = damageCalculationService;
		this.dpsDetailsMapper = dpsDetailsMapper;
	}

	/**
	 * A method that returns the {@link Double} value based on a loadouts damage output.
	 * @param loadoutID The ID of the loadout.
	 * @return A {@link ResponseEntity} containing the {@link Double}'s.
	 * @throws IOException
	 */
	@Operation(summary = "Get the DPS from a loadout.", description = "Retrieves the damage per second of the specific loadout.")
	@GetMapping("/getLoadoutDPS")
	public ResponseEntity<DPSDetailsDTO> getLoadoutDPS(@RequestParam int loadoutID) throws IOException
	{
		DPSDetails dpsDetails = damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout(loadoutID));
		DPSDetailsDTO dpsDetailsDTO = dpsDetailsMapper.convertToDTO(dpsDetails);
		return ResponseEntity.ok(dpsDetailsDTO);
	}

	/**
	 * A method that returns a {@link List} of {@link Double} values based on each users loadouts.
	 * @return A {@link ResponseEntity} containing a {@link List} of {@link Double}'s.
	 * @throws IOException
	 */
	@Operation(summary = "Get the DPS from all loadouts.", description = "Retrieves the damage per second of all loadout tied to a session.")
	@GetMapping("/getAllLoadoutsDPS")
	public ResponseEntity<List<DPSDetailsDTO>> getAllLoadoutsDPS() throws IOException
	{
		List<DPSDetails> dpsDetailsList = new ArrayList<>();
		Set<Loadout> loadouts = loadoutManager.getLoadouts();

		for (Loadout loadout : loadouts)
		{

			dpsDetailsList.add(damageCalculationService.calculateOutgoingDamage(loadout));
		}

		return ResponseEntity.ok(dpsDetailsMapper.convertAllToDTO(dpsDetailsList));
	}
}

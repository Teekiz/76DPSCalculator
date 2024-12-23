package Tekiz._DPSCalculator._DPSCalculator.controller.servicecontrollers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.DamageCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
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
public class DamageCalculationController
{
	private final LoadoutManager loadoutManager;
	private final DamageCalculationService damageCalculationService;

	@Autowired
	public DamageCalculationController(LoadoutManager loadoutManager, DamageCalculationService damageCalculationService)
	{
		this.loadoutManager = loadoutManager;
		this.damageCalculationService = damageCalculationService;
	}

	/**
	 * A method that returns the {@link Double} value based on a loadouts damage output.
	 * @param loadoutID The ID of the loadout.
	 * @return A {@link ResponseEntity} containing the {@link Double}'s.
	 * @throws IOException
	 */
	@GetMapping("/getLoadoutDPS")
	public ResponseEntity<Double> getLoadoutDPS(@RequestParam int loadoutID) throws IOException
	{
		return ResponseEntity.ok(damageCalculationService
			.calculateOutgoingDamage(loadoutManager.getLoadout(loadoutID)));
	}

	/**
	 * A method that returns a {@link List} of {@link Double} values based on each users loadouts.
	 * @return A {@link ResponseEntity} containing a {@link List} of {@link Double}'s.
	 * @throws IOException
	 */
	@GetMapping("/getAllLoadoutsDPS")
	public ResponseEntity<List<Double>> getAllLoadoutsDPS() throws IOException
	{
		List<Double> damageList = new ArrayList<>();
		Set<Loadout> loadouts = loadoutManager.getLoadouts();

		for (Loadout loadout : loadouts)
		{
			damageList.add(damageCalculationService.calculateOutgoingDamage(loadout));
		}

		return ResponseEntity.ok(damageList);
	}
}

package Tekiz._DPSCalculator._DPSCalculator.controller;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("api/loadout/weapon")
public class WeaponController
{
	private final LoadoutManager loadoutManager;

	@Autowired
	public WeaponController(LoadoutManager loadoutManager)
	{

		this.loadoutManager = loadoutManager;
	}

	@GetMapping("/get")
	public ResponseEntity<Weapon> getWeapon() throws IOException
	{
		Weapon weapon = loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon();
		if (weapon == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(weapon);
	}

	@GetMapping("/test")
	public String test()
	{
		return "hi";
	}
}

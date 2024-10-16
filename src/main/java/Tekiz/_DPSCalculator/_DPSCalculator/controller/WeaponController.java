package Tekiz._DPSCalculator._DPSCalculator.controller;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//@RequestMapping("api/loadout/weapon")
public class WeaponController
{
	private final LoadoutManager loadoutManager;
	private final WeaponManager weaponManager;
	@Autowired
	public WeaponController(LoadoutManager loadoutManager, WeaponManager weaponManager)
	{
		log.info("Weapon controller created.");
		this.loadoutManager = loadoutManager;
		this.weaponManager = weaponManager;
	}

	@GetMapping("/get")
	public ResponseEntity<Weapon> getWeapon() throws IOException
	{
		//todo - change
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon("10MMPISTOL", loadout);
		if (loadout.getWeapon() == null)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(loadout.getWeapon());
	}

	@GetMapping("/test")
	public String test()
	{
		return "hi";
	}
}

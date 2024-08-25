package Tekiz._DPSCalculator._DPSCalculator.controller;

import Tekiz._DPSCalculator._DPSCalculator.model.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.services.WeaponLoaderService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weapons")
public class WeaponController
{
	private final WeaponLoaderService weaponLoaderService;

	@Autowired
	public WeaponController(WeaponLoaderService weaponLoaderService)
	{
		this.weaponLoaderService = weaponLoaderService;
	}

	@GetMapping("/create") //@RequestParam String weaponName
	public String createWeapon() throws IOException
	{
		return "Weapon created: " + weaponLoaderService.getWeapon("10MMPISTOL").getWeaponName();
	}
}

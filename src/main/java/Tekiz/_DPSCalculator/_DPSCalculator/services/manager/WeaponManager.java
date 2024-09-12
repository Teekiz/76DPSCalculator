package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.WeaponLoaderService;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class WeaponManager
{
	private Weapon currentWeapon;
	private final WeaponLoaderService weaponLoaderService;
	//add mod manager

	@Autowired
	public WeaponManager(WeaponLoaderService weaponLoaderService)
	{
		this.weaponLoaderService = weaponLoaderService;
	}

	public void setWeapon(String weaponName) throws IOException
	{
		Weapon loadedWeapon = weaponLoaderService.getWeapon(weaponName);
		if (loadedWeapon == null) {
			throw new IllegalArgumentException("Weapon not found: " + weaponName);
		}
		this.currentWeapon = loadedWeapon;
	}

	@PreDestroy
	public void clear()
	{
		this.currentWeapon = null;
	}
}

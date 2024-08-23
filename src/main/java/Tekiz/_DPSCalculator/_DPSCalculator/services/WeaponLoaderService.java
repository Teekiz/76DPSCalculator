package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.Pistol;
import java.io.IOException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Getter
public class WeaponLoaderService
{
	//todo update to weapon list if comparison to multiple is made.
	private final WeaponFactory weaponFactory;
	private final ModService modService;

	private Weapon weapon = null;

	@Autowired
	public WeaponLoaderService(WeaponFactory weaponFactory, ModService modService)
	{
		this.weaponFactory = weaponFactory;
		this.modService = modService;
	}

	public void createWeapon(WeaponType weaponType)
	{
		weapon = weaponFactory.createWeapon(weaponType);

		if (weapon instanceof Pistol)
		{
			((Pistol) weapon).setReceiver(modService.getReceivers().get(0));

		}
	}

}

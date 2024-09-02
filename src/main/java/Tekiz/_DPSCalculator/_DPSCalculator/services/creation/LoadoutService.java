package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoadoutService
{
	private final WeaponLoaderService weaponLoaderService;
	private final ModLoaderService modLoaderService;
	private final PerkLoaderService perkLoaderService;
	private final ConsumableLoaderService consumableLoaderService;

	@Autowired
	public LoadoutService(WeaponLoaderService weaponLoaderService, ModLoaderService modLoaderService, PerkLoaderService perkLoaderService, ConsumableLoaderService consumableLoaderService)
	{
		this.weaponLoaderService = weaponLoaderService;
		this.modLoaderService = modLoaderService;
		this.perkLoaderService = perkLoaderService;
		this.consumableLoaderService = consumableLoaderService;
	}

	public Loadout createNewLoadout()
	{
		return new Loadout(weaponLoaderService,perkLoaderService,modLoaderService,consumableLoaderService);
	}
}

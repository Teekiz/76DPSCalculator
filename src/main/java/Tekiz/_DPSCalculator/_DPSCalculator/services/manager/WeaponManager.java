package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.WeaponLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class WeaponManager implements LoadoutScopeClearable
{
	private Weapon currentWeapon;
	private final WeaponLoaderService weaponLoaderService;
	private final ModLoaderService modLoaderService;
	private final ApplicationEventPublisher applicationEventPublisher;
	private static final Logger logger = LoggerFactory.getLogger(WeaponManager.class);
	//add mod manager

	@Autowired
	public WeaponManager(WeaponLoaderService weaponLoaderService, ModLoaderService modLoaderService, ApplicationEventPublisher applicationEventPublisher)
	{
		this.weaponLoaderService = weaponLoaderService;
		this.modLoaderService = modLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public synchronized void setWeapon(String weaponName) throws IOException
	{
		Weapon loadedWeapon = weaponLoaderService.getWeapon(weaponName);
		if (loadedWeapon != null)
		{
			currentWeapon = loadedWeapon;
			WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(this.currentWeapon, "Weapon has been set.");
			logger.debug("WeaponChangedEvent has been created for weapon {}.", this.currentWeapon);
			applicationEventPublisher.publishEvent(weaponChangedEvent);
		}
		else {
			logger.error("Weapon loading failed for: " + weaponName);
		}
	}

	public synchronized void modifyWeapon(String modName, ModType modType) throws IOException
	{
		Weapon weapon = this.currentWeapon;
		if (weapon instanceof RangedWeapon)
		{
			switch (modType)
			{
				case RECEIVER -> {
					((RangedWeapon) weapon).setMod(modLoaderService.getReceiver(modName));
				}
			}
		}
		WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(this.currentWeapon, "Weapon has been modified.");
		logger.debug("WeaponChangedEvent has been created. Weapon {} has been modified.", this.currentWeapon);
		applicationEventPublisher.publishEvent(weaponChangedEvent);
		//todo - create
		//else if (weapon instanceof MeleeWeapon)
	}

	@PreDestroy
	public void clear()
	{
		this.currentWeapon = null;
	}
}

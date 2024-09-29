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
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.RangedMod;

/**
 * A service used to manage {@link Weapon} objects.
 */
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

	/**
	 * The constructor for a {@link WeaponManager} object.
	 * @param weaponLoaderService A service used to load and create {@link Weapon} objects.
	 * @param modLoaderService A service used to load and create {@link RangedMod} objects.
	 * @param applicationEventPublisher The event publisher for dispatching {@link WeaponChangedEvent}s.
	 */
	@Autowired
	public WeaponManager(WeaponLoaderService weaponLoaderService, ModLoaderService modLoaderService, ApplicationEventPublisher applicationEventPublisher)
	{
		this.weaponLoaderService = weaponLoaderService;
		this.modLoaderService = modLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Loads a weapon by its name and sets it as the current weapon.
	 * If the weapon is successfully loaded, a {@link WeaponChangedEvent} is published.
	 *
	 * @param weaponName The name of the weapon to load.
	 * @throws IOException If the weapon cannot be loaded.
	 */
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

	/**
	 * Modifies the current weapon by applying a specified modification (mod),
	 * such as changing the receiver of a ranged weapon. After modification, a {@link WeaponChangedEvent} is published.
	 *
	 * @param modName The name of the mod to apply.
	 * @param modType The type of mod being applied (e.g., receiver).
	 * @throws IOException If the mod cannot be loaded.
	 */
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

	/**
	 * A method used during the cleanup of this service.
	 */
	@PreDestroy
	public void clear()
	{
		this.currentWeapon = null;
	}
}

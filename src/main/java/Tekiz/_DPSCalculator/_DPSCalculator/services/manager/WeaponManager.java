package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.WeaponLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.RangedMod;

/**
 * A service used to manage {@link Weapon} objects.
 */
@Service
@Getter
@Slf4j
public class WeaponManager
{
	private final WeaponLoaderService weaponLoaderService;
	private final ModLoaderService modLoaderService;
	private final ApplicationEventPublisher applicationEventPublisher;
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
	@SaveLoadout
	public synchronized void setWeapon(String weaponName, Loadout loadout) throws IOException
	{
		Weapon newWeapon = weaponLoaderService.getWeapon(weaponName);
		if (newWeapon != null)
		{
			loadout.setWeapon(newWeapon);
			WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(newWeapon, loadout,"Weapon has been set.");
			log.debug("WeaponChangedEvent has been created for weapon {}.", newWeapon);
			applicationEventPublisher.publishEvent(weaponChangedEvent);
		}
		else {
			log.error("Weapon loading failed for: " + weaponName);
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
	@SaveLoadout
	public synchronized void modifyWeapon(String modName, ModType modType, Loadout loadout) throws IOException
	{
		Weapon weapon = loadout.getWeapon();
		if (weapon instanceof RangedWeapon)
		{
			switch (modType)
			{
				case RECEIVER -> {
					((RangedWeapon) weapon).setMod(modLoaderService.getReceiver(modName));
				}
			}
		}
		WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(weapon,  loadout, "Weapon has been modified.");
		log.debug("WeaponChangedEvent has been created. Weapon {} has been modified.", weapon);
		applicationEventPublisher.publishEvent(weaponChangedEvent);
		//todo - create
		//else if (weapon instanceof MeleeWeapon)
	}
}

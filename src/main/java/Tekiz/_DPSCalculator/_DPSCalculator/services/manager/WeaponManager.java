package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Weapon} objects.
 */
@Service
@Getter
@Slf4j
public class WeaponManager
{
	private final DataLoaderService dataLoaderService;
	private final WeaponFactory weaponFactory;
	private final ApplicationEventPublisher applicationEventPublisher;
	//add mod manager

	/**
	 * The constructor for a {@link WeaponManager} object.
	 * @param dataLoaderService A service used to load and create {@link Weapon} objects.
	 * @param applicationEventPublisher The event publisher for dispatching {@link WeaponChangedEvent}s.
	 */
	@Autowired
	public WeaponManager(DataLoaderService dataLoaderService, WeaponFactory weaponFactory, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.weaponFactory = weaponFactory;
		this.applicationEventPublisher = applicationEventPublisher;
	}
	/**
	 * Loads a weapon by its name and sets it as the current weapon.
	 * If the weapon is successfully loaded, a {@link WeaponChangedEvent} is published.
	 *
	 * @param weaponID The name of the weapon to load.
	 * @throws IOException If the weapon cannot be loaded.
	 */
	@SaveLoadout
	public synchronized void setWeapon(String weaponID, Loadout loadout) throws IOException
	{
		Weapon newWeapon = dataLoaderService.loadData(weaponID, Weapon.class, weaponFactory);

		if (newWeapon != null)
		{
			loadout.setWeapon(newWeapon);
			WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(newWeapon, loadout,"Weapon has been set.");
			log.debug("WeaponChangedEvent has been created for weapon {}.", newWeapon);
			applicationEventPublisher.publishEvent(weaponChangedEvent);
		}
		else {
			log.error("Weapon loading failed for: " + weaponID);
		}
	}
	/**
	 * Modifies the current weapon by applying a specified modification (mod),
	 * such as changing the receiver of a ranged weapon. After modification, a {@link WeaponChangedEvent} is published.
	 *
	 * @param modID The name of the mod to apply.
	 * @param modType The type of mod being applied (e.g., receiver).
	 * @throws IOException If the mod cannot be loaded.
	 */
	@SaveLoadout
	public synchronized void modifyWeapon(String modID, ModType modType, Loadout loadout) throws IOException
	{
		Weapon weapon = loadout.getWeapon();
		if (weapon instanceof RangedWeapon)
		{
			switch (modType)
			{
				case RECEIVER -> {
					((RangedWeapon) weapon).setMod(dataLoaderService.loadData(modID, Receiver.class, null));
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

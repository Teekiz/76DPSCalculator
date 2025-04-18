package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
	 * @throws ResourceNotFoundException If the weapon cannot be found.
	 */
	@SaveLoadout
	public synchronized void setWeapon(String weaponID, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Weapon weapon = dataLoaderService.loadData(weaponID, Weapon.class, weaponFactory);

		if (weapon == null){
			log.error("Weapon loading failed for: {}.", weaponID);
			throw new ResourceNotFoundException("Weapon not found while setting weapon. Weapon ID: " + weaponID  + ".");
		}

		loadout.setWeapon(weapon);
		WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(weapon, loadout,"Weapon has been set.");
		log.debug("WeaponChangedEvent has been created for weapon {}.", weapon);
		applicationEventPublisher.publishEvent(weaponChangedEvent);
	}
	/**
	 * Modifies the current weapon by applying a specified modification (mod),
	 * such as changing the receiver of a ranged weapon. After modification, a {@link WeaponChangedEvent} is published.
	 *
	 * @param modID The name of the mod to apply.
	 * @throws IOException If the mod cannot be loaded.
	 * @throws ResourceNotFoundException If the weapon is not set or the mod cannot be found.
	 * @return {@code true} if the mod was applied successfully.
	 */
	@SaveLoadout
	public synchronized boolean modifyWeapon(String modID, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Weapon weapon = loadout.getWeapon();
		WeaponMod weaponMod = dataLoaderService.loadData(modID, WeaponMod.class, null);

		if (weapon == null || weaponMod == null){
			if (weapon == null){
				log.error("Weapon not found during modification.");
			} else {
				log.error("Weapon mod loading failed for: {}.", modID);
			}
			throw new ResourceNotFoundException("Weapon or mod not found during weapon modification. Modification ID: " + modID + ".");
		}

		if (weapon.setMod(weaponMod)){
			WeaponChangedEvent weaponChangedEvent = new WeaponChangedEvent(weapon,  loadout, "Weapon has been modified.");
			log.debug("WeaponChangedEvent has been created. Weapon {} has been modified.", weapon);
			applicationEventPublisher.publishEvent(weaponChangedEvent);
			return true;
		}

		return false;
	}

	/**
	 * A method used to get all available mods for a weapon, or all available mods if {@code modType} is not null.
	 * @param weapon The weapon used to determine available mods for.
	 * @param modType The type of mods to filter for.
	 * @return A {@link List} of available weapon mods, filtered by modType if {@code not null},
	 * @throws IOException If the mod data cannot be loaded.
	 */
	public List<WeaponMod> getAvailableWeaponMods(Weapon weapon, ModType modType) throws IOException
	{
		if (weapon == null || weapon.getModifications() == null){
			return Collections.emptyList();
		}

		Set<String> availableModsForWeapon = weapon.getModifications()
			.entrySet()
			.stream()
			.filter(mod -> modType == null || mod.getKey().equals(modType))
			.flatMap(entry -> entry.getValue().getAvailableInSlot().stream())
			.map(String::toLowerCase)
			.collect(Collectors.toSet());

		return dataLoaderService.loadAllData("WEAPONMODS", WeaponMod.class, null)
			.stream()
			.filter(weaponMod -> modType == null || weaponMod.modType().equals(modType))
			.filter(weaponMod -> availableModsForWeapon.contains(weaponMod.alias().toLowerCase()))
			.collect(Collectors.toList());
	}
}

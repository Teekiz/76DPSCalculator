package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ArmourFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ArmourChangedEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Armour} objects.
 */
@Slf4j
@Service
@Getter
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ArmourManager
{
	private final DataLoaderService dataLoaderService;
	private final ArmourFactory armourFactory;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Loads a piece of armour by its ID and adds it to the current armour set.
	 *
	 * @param armourID The name of the armour to load.
	 * @param armourSlot The slot where the armour will be applied to.
	 * @throws IOException If the armour cannot be loaded.
	 * @return {@code true} if the armour has been added successfully.
	 */
	@SaveLoadout
	public synchronized boolean addArmour(String armourID, ArmourSlot armourSlot, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Armour armour = dataLoaderService.loadData(armourID, Armour.class, armourFactory);

		if (armour == null){
			log.error("Armour loading failed for: {}", armourID);
			throw new ResourceNotFoundException("Armour not found while adding armour. Armour ID: " + armourID  + ".");
		} else if (!armour.getArmourPiece().equals(armourSlot.getArmourPiece())){
			log.error("Armour {} cannot be placed slot {}.", armour.getName(), armourSlot);
			return false;
		}

		armour.setArmourSlot(armourSlot);
		loadout.getArmour().addArmour(armour);

		ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(armour, loadout,"Armour has been updated.");
		log.debug("ArmourChangedEvent has been created for armour {}.", armour);
		applicationEventPublisher.publishEvent(armourChangedEvent);
		return true;
	}

	/**
	 * Removes the armour from the armour slot.
	 * @param armourType The type of armour to be removed.
	 * @param armourSlot The slot the armour takes up.
	 * @param loadout The loadout to remove the armour from.
	 */
	@SaveLoadout
	public synchronized void removeArmour(ArmourType armourType, ArmourSlot armourSlot, Loadout loadout)
	{
		loadout.getArmour().removeArmour(armourType, armourSlot);

		ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(this, loadout,"Armour has been updated.");
		log.debug("ArmourChangedEvent has been created as armour slot has been removed {}.", armourSlot);
		applicationEventPublisher.publishEvent(armourChangedEvent);
	}

	/**
	 * Modifies the current armour in the slot by applying a specified modification (mod).
	 * @param modID The name of the mod to apply.
	 * @param armourType The type of armour being modified.
	 * @param armourSlot The slot where the armour will be applied to.
	 * @param loadout The loadout to be modified.
	 * @throws IOException If the mod cannot be loaded.
	 * @return {@code true} if the modification was successful.
	 */
	@SaveLoadout
	public synchronized boolean modifyArmour(String modID, ArmourType armourType, ArmourSlot armourSlot, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		ArmourMod armourMod = dataLoaderService.loadData(modID, ArmourMod.class, null);

		if (armour == null || armourMod == null){
			if (armour == null){
				log.error("Armour not found during modification.");
			} else {
				log.error("Armour mod loading failed for: {}.", modID);
			}
			throw new ResourceNotFoundException("Armour or mod not found during armour modification. Modification ID: " + modID + ". Armour piece: " + armourSlot + " for " + armourType + ".");
		}

		if (armour.setMod(armourMod)){
			ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(this, loadout,"Armour has been updated.");
			log.debug("ArmourChangedEvent has been created. Armour {} has been modified.", armour.getName());
			applicationEventPublisher.publishEvent(armourChangedEvent);
			return true;
		}
		return false;
	}

	/**
	 * A method used to get all armour pieces, filtered by the {@code armourType} and {@code armourPiece}.
	 * @param armourType The type of armour to filter for ({@code null} to return all armour regardless of type).
	 * @param armourPiece The slot where the armour can be placed to filter for ({@code null} to return all armour regardless of type).
	 * @return A {@link List} of available armour pieces, filtered by armourType and armourPiece.
	 * @throws IOException If the armour data cannot be loaded.
	 */
	public List<Armour> getAvailableArmour(ArmourType armourType, ArmourPiece armourPiece) throws IOException
	{
		return dataLoaderService.loadAllData("ARMOUR", Armour.class, armourFactory)
			.stream()
			.filter(armour -> armourType == null || armour.getArmourType().equals(armourType))
			.filter(armour -> armourPiece == null || armour.getArmourPiece().equals(armourPiece))
			.collect(Collectors.toList());
	}

	/**
	 * A method used to get all available mods for a weapon, or all available mods if {@code modType} is not null.
	 * @param armour The armour used to determine available mods for.
	 * @param modType The type of mods to filter for.
	 * @return A {@link List} of available armour mods, filtered by modType if {@code not null},
	 * @throws IOException If the mod data cannot be loaded.
	 */
	public List<ArmourMod> getAvailableArmourMods(Armour armour, ModType modType) throws IOException
	{
		if (armour == null || armour.getModifications() == null){
			return Collections.emptyList();
		}

		Set<String> availableModsForArmour = armour.getModifications()
			.entrySet()
			.stream()
			.filter(mod -> modType == null || mod.getKey().equals(modType))
			.flatMap(entry -> entry.getValue().getAvailableInSlot().stream())
			.map(String::toLowerCase)
			.collect(Collectors.toSet());

		return dataLoaderService.loadAllData("ARMOURMODS", ArmourMod.class, null)
			.stream()
			.filter(armourMod -> modType == null || armourMod.modType().equals(modType))
			.filter(armourMod -> availableModsForArmour.contains(armourMod.alias().toLowerCase()))
			.collect(Collectors.toList());
	}
}

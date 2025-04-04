package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ArmourFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ArmourChangedEvent;
import java.io.IOException;
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
	 */
	@SaveLoadout
	public synchronized void addArmour(String armourID, ArmourSlot armourSlot, Loadout loadout) throws IOException
	{
		Armour armour = dataLoaderService.loadData(armourID, Armour.class, armourFactory);

		if (armour == null){
			return;
		}

		armour.setArmourSlot(armourSlot);
		loadout.getArmour().addArmour(armour);

		ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(armour, loadout,"Armour has been updated.");
		log.debug("ArmourChangedEvent has been created for armour {}.", armour);
		applicationEventPublisher.publishEvent(armourChangedEvent);
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
	 */
	@SaveLoadout
	public synchronized void modifyArmour(String modID, ArmourType armourType, ArmourSlot armourSlot, Loadout loadout) throws IOException
	{
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		ArmourMod armourMod = dataLoaderService.loadData(modID, ArmourMod.class, null);

		if (armour == null){
			return;
		}

		armour.setMod(armourMod);

		ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(this, loadout,"Armour has been updated.");
		log.debug("ArmourChangedEvent has been created. Armour {} has been modified.", armour.getName());
		applicationEventPublisher.publishEvent(armourChangedEvent);
	}

	/**
	 * Modifies the current armour in the slot by applying a specified modification (mod).
	 * @param effectID The ID of the legendary effect to be applied.
	 * @param armourType The type of armour being modified.
	 * @param armourSlot The slot where the armour will be applied to.
	 * @param loadout The loadout to be modified.
	 * @throws IOException If the mod cannot be loaded.
	 */
	@SaveLoadout
	public synchronized void changeArmourLegendary(String effectID, ArmourType armourType, ArmourSlot armourSlot, Loadout loadout) throws IOException
	{
		Armour armour = loadout.getArmour().getArmourInSlot(armourType, armourSlot);
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(effectID, LegendaryEffect.class, null);

		if (armour == null || armour.getLegendaryEffects() == null){
			return;
		}

		armour.getLegendaryEffects().addLegendaryEffect(legendaryEffect);
		ArmourChangedEvent armourChangedEvent = new ArmourChangedEvent(armour, loadout,"Armour has been updated.");
		log.debug("ArmourChangedEvent has been created. Armour {} has been modified. Added new legendary effect: {}.", armour.getName(), effectID);
		applicationEventPublisher.publishEvent(armourChangedEvent);

	}
}

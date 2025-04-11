package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectObject;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LegendaryEffectManager
{
	private final DataLoaderService dataLoaderService;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public LegendaryEffectManager(DataLoaderService dataLoaderService, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@SaveLoadout
	public boolean addLegendaryEffect(String legendaryEffectID, LegendaryEffectObject legendaryEffectObject, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(legendaryEffectID, LegendaryEffect.class, null);

		if (legendaryEffect == null || legendaryEffectObject == null || legendaryEffectObject.getLegendaryEffects() == null){
			if (legendaryEffect == null){
				log.error("Legendary effect loading failed for: {}", legendaryEffectID);
			} else if (legendaryEffectObject == null) {
				log.error("Object to add legendary effect not found.");
			} else {
				log.error("Object does not have legendary effect slot.");
			}
			throw new ResourceNotFoundException("Cannot add effect to object. Effect ID: " + legendaryEffectID + ".");
		}

		if (!legendaryEffect.doesLegendaryObjectMatchType(legendaryEffectObject)){
			log.error("Legendary effect {} could not be applied to object {}. (Incompatible types)", legendaryEffect.name(), legendaryEffectObject.getName());
			return false;
		}

		if(legendaryEffectObject.getLegendaryEffects().addLegendaryEffect(legendaryEffect)){
			log.debug("Added legendary effect {} to item: {}.", legendaryEffect.name(), legendaryEffectObject);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(legendaryEffect, loadout,legendaryEffect.name() + " has been added.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
			return true;
		}
		return false;
	}

	@SaveLoadout
	public boolean removeLegendaryEffect(StarType starType, LegendaryEffectObject legendaryEffectObject, Loadout loadout) throws ResourceNotFoundException
	{
		if (legendaryEffectObject == null || legendaryEffectObject.getLegendaryEffects() == null)
		{
			throw new ResourceNotFoundException("Could not remove effect from object. Star type: " + starType + ".");
		}

		if (legendaryEffectObject.getLegendaryEffects().removeLegendaryEffect(starType)){
			log.debug("Removed {} legendary effect from item: {}.", starType, legendaryEffectObject.getName());
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(starType, loadout,starType + " has been removed.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
			return true;
		}
		return false;
	}

	/**
	 * A method used to filter get all available legendary effects.
	 * @param starType The star level of the legendary effect.
	 * @param category The category the legendary effect can be applied to.
	 * @return A {@link List} of {@link LegendaryEffect}s, filtered if provided by {@code starType} and {@code category}.
	 * @throws IOException If the legendary effect objects cannot be loaded.
	 */
	public List<LegendaryEffect> getAvailableLegendaryEffects(StarType starType, Category category) throws IOException
	{
		List<LegendaryEffect> legendaryEffects = dataLoaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null);

		if (legendaryEffects == null){
			return List.of();
		}

		return legendaryEffects
			.stream()
			.filter(legendaryEffect -> starType == null || legendaryEffect.starType().equals(starType))
			.filter(legendaryEffect -> category == null || legendaryEffect.categories().contains(category))
			.collect(Collectors.toList());
	}
}
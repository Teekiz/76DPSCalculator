package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectCompatible;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import java.io.IOException;
import java.util.List;
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
	public void changeLegendaryEffect(String legendaryEffectID, StarType starType, LegendaryEffectCompatible legendaryEffectCompatible, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(legendaryEffectID, LegendaryEffect.class, null);

		if (legendaryEffectCompatible == null){
			log.error("Object to add legendary effect not found.");
			throw new ResourceNotFoundException("Cannot add effect to object. Effect ID: " + legendaryEffectID + ".");
		}

		legendaryEffectCompatible.modifyLegendaryEffect(starType, legendaryEffect);
		log.debug("Added legendary effect {} to item: {}.", legendaryEffect.name(), legendaryEffectCompatible);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(legendaryEffect, loadout, legendaryEffect.name() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
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
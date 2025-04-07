package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectObject;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import java.io.IOException;
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
	public void addLegendaryEffect(String legendaryEffectID, LegendaryEffectObject legendaryEffectObject, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(legendaryEffectID, LegendaryEffect.class, null);

		if (legendaryEffect == null || legendaryEffectObject == null||legendaryEffectObject.getLegendaryEffects() == null){
			if (legendaryEffect == null){
				log.error("Legendary effect loading failed for: {}", legendaryEffectID);
			} else if (legendaryEffectObject == null) {
				log.error("Object to add legendary effect not found.");
			} else {
				log.error("Object does not have legendary effect slot.");
			}
			throw new ResourceNotFoundException("Cannot add effect to object. Effect ID: " + legendaryEffectID + ".");
		}

		legendaryEffectObject.getLegendaryEffects().addLegendaryEffect(legendaryEffect);

		log.debug("Added legendary effect {} to item: {}.", legendaryEffect.name(), legendaryEffectObject);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(legendaryEffect, loadout,legendaryEffect.name() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}

	@SaveLoadout
	public void removeLegendaryEffect(String legendaryEffectID, LegendaryEffectObject legendaryEffectObject, Loadout loadout) throws IOException
	{
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(legendaryEffectID, LegendaryEffect.class, null);

		if (legendaryEffect == null)
		{
			return;
		}

		legendaryEffectObject.getLegendaryEffects().removeLegendaryEffect(legendaryEffect);

		log.debug("Removed legendary effect {} from item: {}.", legendaryEffect.name(), legendaryEffectObject);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(legendaryEffect, loadout,legendaryEffect.name() + " has been removed.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
}
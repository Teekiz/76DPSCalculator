package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ConditionChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.consumable.ConsumableLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class ConsumableManager implements LoadoutScopeClearable
{
	//there can be many food effects but only one of each alcohol and chem.
	private Set<Consumable> consumables;
	private final ConsumableLoaderService consumableLoaderService;
	private final ConsumableLogic consumableLogic;
	private final ApplicationEventPublisher applicationEventPublisher;
	private static final Logger logger = LoggerFactory.getLogger(ConsumableManager.class);

	@Autowired
	public ConsumableManager(ConsumableLoaderService consumableLoaderService, ConsumableLogic consumableLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
		this.consumables = new HashSet<>();
		this.consumableLoaderService = consumableLoaderService;
		this.consumableLogic = consumableLogic;
	}

	public void addConsumable(String consumableName) throws IOException
	{
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		consumables.add(consumable);
		checkAndApplyConsumable(consumable);
	}
	public void removeConsumable(Consumable consumable)
	{
		consumables.remove(consumable);
		publishEvent(consumable);
	}

	//this is used to apply effects after an event has been called
	public void checkConsumable(Consumable consumable)
	{
		if (consumableLogic.evaluateCondition(consumable)) {
			consumableLogic.applyConditionEffect(consumable);
		} else {
			publishEvent(consumable);
		}
	}

	public void checkAndApplyConsumable(Consumable consumable)
	{
		if (consumableLogic.evaluateCondition(consumable))
		{
			consumableLogic.applyConditionEffect(consumable);
		}
		consumableLogic.applyEffect(consumable);
	}

	public void publishEvent(Consumable consumable)
	{
		ConditionChangedEvent conditionChangedEvent = new ConditionChangedEvent(consumable, consumable.getConsumableName());
		logger.debug("PerkChangedEvent has been created for perk {}.", consumable);
		applicationEventPublisher.publishEvent(conditionChangedEvent);
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		this.consumables.forEach(this::checkConsumable);
	}

	@PreDestroy
	public void clear()
	{
		if (this.consumables != null) {
			this.consumables.clear();
		}
		this.consumables = null;
	}
}

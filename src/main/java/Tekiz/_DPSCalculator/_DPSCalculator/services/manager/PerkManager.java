package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ConditionChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashSet;
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
public class PerkManager implements LoadoutScopeClearable
{
	private HashSet<Perk> perks;
	private final PerkLoaderService perkLoaderService;
	private final PerkLogic perkLogic;
	private final ApplicationEventPublisher applicationEventPublisher;
	private static final Logger logger = LoggerFactory.getLogger(PerkManager.class);

	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService, PerkLogic perkLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.perks = new HashSet<>();
		this.perkLoaderService = perkLoaderService;
		this.perkLogic = perkLogic;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	//when a perk is added - it is automatically added to the effects.
	public void addPerk(String perkName) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		perks.add(perk);
		processPerk(perk);
	}

	public void removePerk(String perkName) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		perks.remove(perk);
		publishEvent(perk);
	}

	public synchronized void processPerk(Perk perk)
	{
		if (perkLogic.evaluateCondition(perk))
		{
			perkLogic.applyEffect(perk);
		}
		else
		{
			publishEvent(perk);
		}
	}

	public void publishEvent(Perk perk)
	{
		ConditionChangedEvent conditionChangedEvent = new ConditionChangedEvent(perk, perk.getPerkName());
		logger.debug("ConditionChangedEvent has been created for perk {}.", perk);
		applicationEventPublisher.publishEvent(conditionChangedEvent);
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		this.perks.forEach(this::processPerk);
	}

	@PreDestroy
	public void clear()
	{
		if (this.perks != null) {
			this.perks.clear();
		}
		this.perks = null;
	}
}

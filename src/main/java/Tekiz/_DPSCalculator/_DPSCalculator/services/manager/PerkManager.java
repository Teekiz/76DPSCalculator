package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.perks.PerkLogic;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class PerkManager
{
	private Set<Perk> perks;
	private final PerkLoaderService perkLoaderService;
	private final PerkLogic perkLogic;

	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService, PerkLogic perkLogic)
	{
		this.perks = new HashSet<>();
		this.perkLoaderService = perkLoaderService;
		this.perkLogic = perkLogic;
	}

	public void addPerk(String perkName) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		processPerk(perk);
		perks.add(perk);
	}

	public void processPerk(Perk perk)
	{
		if (perkLogic.evaluateCondition(perk))
		{
			perkLogic.applyEffect(perk);
		}
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		perks.forEach(this::processPerk);
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

package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierLogic;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class PerkManager implements LoadoutScopeClearable
{
	private HashMap<Perk, Boolean> perks;
	private final PerkLoaderService perkLoaderService;
	private final ModifierLogic modifierLogic;
	private static final Logger logger = LoggerFactory.getLogger(PerkManager.class);

	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService, ModifierLogic modifierLogic)
	{
		this.perks = new HashMap();
		this.perkLoaderService = perkLoaderService;
		this.modifierLogic = modifierLogic;
	}

	//when a perk is added - it is automatically added to the effects.
	public void addPerk(String perkName) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		perks.put(perk, modifierLogic.evaluateCondition(perk));
	}

	//todo - consider changing from different perkNames (as the list doesn't match)
	public void removePerk(String perkName) throws IOException
	{
		perks.entrySet().removeIf(perk -> perk.getKey().getName().equals(perkName));
	}

	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		for (Map.Entry<Perk, Boolean> entry : perks.entrySet())
		{
			Boolean newValue = modifierLogic.evaluateCondition(entry.getKey());
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
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

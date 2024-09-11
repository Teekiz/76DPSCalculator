package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponModifiedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponModifiedListener;
import jakarta.annotation.PreDestroy;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
@Getter
public class PerkManager implements WeaponModifiedListener
{
	private Set<Perk> perks;
	private final PerkLoaderService perkLoaderService;

	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService)
	{
		this.perks = new HashSet<>();
		this.perkLoaderService = perkLoaderService;
	}

	@Override
	public void onWeaponModified(WeaponModifiedEvent event)
	{

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

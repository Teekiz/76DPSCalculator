package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerkFactory
{
	private final PerkLoaderService perkLoaderService;

	@Lazy
	@Autowired
	public PerkFactory(PerkLoaderService perkLoaderService)
	{
		this.perkLoaderService = perkLoaderService;
	}
	public Perk createPerk(String perkName) throws IOException
	{
		return perkLoaderService.getPerk(perkName);
	}
}

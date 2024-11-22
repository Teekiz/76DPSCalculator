package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerkFactory
{
	private final DataLoaderService dataLoaderService;

	@Lazy
	@Autowired
	public PerkFactory(DataLoaderService dataLoaderService)
	{
		this.dataLoaderService = dataLoaderService;
	}
	public Perk createPerk(String perkID, int currentRank) throws IOException
	{
		Perk perk = dataLoaderService.loadData(perkID, Perk.class, null);
		perk.perkRank().setCurrentRank(currentRank);
		return perk;
	}
}

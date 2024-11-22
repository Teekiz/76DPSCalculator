package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumableFactory
{
	private final DataLoaderService dataLoaderService;

	@Lazy
	@Autowired
	public ConsumableFactory(DataLoaderService dataLoaderService)
	{
		this.dataLoaderService = dataLoaderService;
	}
	public Consumable createConsumable(String consumableID) throws IOException
	{
		return dataLoaderService.loadData(consumableID, Consumable.class, null);
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ConsumableLoaderService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumableFactory
{
	private final ConsumableLoaderService consumableLoaderService;

	@Lazy
	@Autowired
	public ConsumableFactory(ConsumableLoaderService consumableLoaderService)
	{
		this.consumableLoaderService = consumableLoaderService;
	}
	public Consumable createConsumable(String consumableName) throws IOException
	{
		return consumableLoaderService.getConsumable(consumableName);
	}
}

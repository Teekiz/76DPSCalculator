package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
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
public class MutationFactory
{
	private final DataLoaderService dataLoaderService;
	@Lazy
	@Autowired
	public MutationFactory(DataLoaderService dataLoaderService)
	{
		this.dataLoaderService = dataLoaderService;
	}
	public Mutation createMutation(String mutationID) throws IOException
	{
		return dataLoaderService.loadData(mutationID, Mutation.class, null);
	}
}

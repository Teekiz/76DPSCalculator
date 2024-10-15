package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.MutationLoaderService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MutationFactory
{
	private final MutationLoaderService mutationLoaderService;
	@Lazy
	@Autowired
	public MutationFactory(MutationLoaderService mutationLoaderService)
	{
		this.mutationLoaderService = mutationLoaderService;
	}
	public Mutation createMutation(String mutationName) throws IOException
	{
		return mutationLoaderService.getMutation(mutationName);
	}
}

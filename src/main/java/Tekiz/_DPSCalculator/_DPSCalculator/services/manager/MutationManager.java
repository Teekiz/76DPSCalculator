package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.MutationLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierLogic;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class MutationManager implements LoadoutScopeClearable
{
	private Set<Mutation> mutations;
	private final MutationLoaderService mutationLoaderService;
	private final ModifierLogic modifierLogic;

	@Autowired
	public MutationManager(MutationLoaderService mutationLoaderService, ModifierLogic modifierLogic)
	{
		this.mutations = new HashSet<>();
		this.mutationLoaderService = mutationLoaderService;
		this.modifierLogic = modifierLogic;
	}

	public void addMutation(String mutationName) throws IOException
	{
		Mutation mutation = mutationLoaderService.getMutation(mutationName);
		mutations.add(mutation);
	}

	public void removeMutation(String mutationName) throws IOException
	{
		mutations.removeIf(mutation -> mutation.getName().equals(mutationName));
	}

	public HashMap<Modifier, Boolean> getMutationModifiers()
	{
		HashMap<Modifier, Boolean> mutationMap = new HashMap<>();
		for (Mutation mutation : mutations)
		{
			if (mutation.getPositiveEffects() != null) {
				mutationMap.put(mutation.getPositiveEffects(), true);
			}

			if (mutation.getNegativeEffects() != null) {
				mutationMap.put(mutation.getNegativeEffects(), true);
			}
		}
		return mutationMap;
	}

	@PreDestroy
	public void clear()
	{
		if (this.mutations != null)
		{
			this.mutations.clear();
		}
		this.mutations = null;
	}
}




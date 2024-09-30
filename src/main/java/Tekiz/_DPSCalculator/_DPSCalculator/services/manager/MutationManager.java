package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.MutationLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Mutation} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class MutationManager implements LoadoutScopeClearable
{
	private Set<Mutation> mutations;
	private final MutationLoaderService mutationLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link MutationManager} object.
	 * @param mutationLoaderService A service used to load {@link Mutation} objects.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public MutationManager(MutationLoaderService mutationLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
		this.mutations = new HashSet<>();
		this.mutationLoaderService = mutationLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	public void addMutation(String mutationName) throws IOException
	{
		Mutation mutation = mutationLoaderService.getMutation(mutationName);
		mutations.add(mutation);

		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, mutation.getName() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}

	public void removeMutation(String mutationName) throws IOException
	{
		Mutation mutation = mutations.stream()
			.filter(object -> object.getName().equals(mutationName))
			.findFirst()
			.orElse(null);
		if (mutation != null)
		{
			mutations.remove(mutation);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, mutation.getName() + " has been removed.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}

	/**
	 * A method that aggregates and returns all positive and negative {@link Mutation} {@link Modifier}s.
	 * @return A {@link HashMap} of {@link Modifier}s and condition values.
	 */
	public HashMap<Modifier, Boolean> getMutationModifiers()
	{
		HashMap<Modifier, Boolean> mutationMap = new HashMap<>();
		for (Mutation mutation : mutations)
		{
			if (mutation.getNegativeEffects() != null) {
				mutationMap.put(mutation.getNegativeEffects(), true);
			}
			if (mutation.getPositiveEffects() != null) {
				mutationMap.put(mutation.getPositiveEffects(), true);
			}
		}
		return mutationMap;
	}

	/**
	 * A method used during the cleanup of this service.
	 */
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




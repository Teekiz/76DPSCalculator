package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.MutationLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Mutation} objects.
 */
@Service
@Getter
public class MutationManager
{
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
		this.mutationLoaderService = mutationLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
	public Set<Mutation> getMutations()
	{
		return getLoadoutManager().getActiveLoadout().getMutations();
	}
	public void addMutation(String mutationName) throws IOException
	{
		Mutation mutation = mutationLoaderService.getMutation(mutationName);
		getMutations().add(mutation);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, mutation.getName() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	public void removeMutation(String mutationName) throws IOException
	{
		Mutation mutation = getMutations()
			.stream()
			.filter(object -> object.getName().equals(mutationName))
			.findFirst()
			.orElse(null);
		if (mutation != null)
		{
			getMutations().remove(mutation);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, mutation.getName() + " has been removed.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}
}




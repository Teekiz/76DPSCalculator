package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.MutationLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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

	public void addMutation(String mutationName, Loadout loadout) throws IOException
	{
		Mutation mutation = mutationLoaderService.getMutation(mutationName);
		loadout.getMutations().add(mutation);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, loadout,mutation.name() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	public void removeMutation(String mutationName, Loadout loadout) throws IOException
	{
		Mutation mutation = loadout.getMutations()
			.stream()
			.filter(object -> object.name().equals(mutationName))
			.findFirst()
			.orElse(null);
		if (mutation != null)
		{
			loadout.getMutations().remove(mutation);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, loadout,mutation.name() + " has been removed.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}
}




package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.Optional;
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
	private final DataLoaderService dataLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link MutationManager} object.
	 * @param dataLoaderService A service used to load {@link Mutation} objects.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public MutationManager(DataLoaderService dataLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	@SaveLoadout
	public void addMutation(String mutationID, Loadout loadout) throws IOException
	{
		Mutation mutation = dataLoaderService.loadData(mutationID, Mutation.class, null);
		loadout.getMutations().add(mutation);
		ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, loadout,mutation.id() + " has been added.");
		applicationEventPublisher.publishEvent(modifierChangedEvent);
	}
	@SaveLoadout
	public void removeMutation(String mutationID, Loadout loadout) throws IOException
	{
		Mutation mutation = loadout.getMutations()
			.stream()
			.filter(object -> object.id().equalsIgnoreCase(mutationID))
			.findFirst()
			.orElse(null);
		if (mutation != null)
		{
			loadout.getMutations().remove(mutation);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(mutation, loadout,mutation.id() + " has been removed.");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}
}




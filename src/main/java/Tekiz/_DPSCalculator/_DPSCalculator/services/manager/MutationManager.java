package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Mutation} objects.
 */
@Slf4j
@Service
@Getter
public class MutationManager
{
	private final DataLoaderService dataLoaderService;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor for a {@link MutationManager} object.
	 * @param dataLoaderService A service used to load {@link Mutation} objects.
	 */
	@Autowired
	public MutationManager(DataLoaderService dataLoaderService, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@SaveLoadout
	public void addMutation(String mutationID, Loadout loadout) throws IOException, ResourceNotFoundException
	{
		Mutation mutation = dataLoaderService.loadData(mutationID, Mutation.class, null);

		if (mutation == null){
			log.error("Mutation loading failed for: {}", mutationID);
			throw new ResourceNotFoundException("Mutation not found. ID: " + mutationID  + ".");
		}

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




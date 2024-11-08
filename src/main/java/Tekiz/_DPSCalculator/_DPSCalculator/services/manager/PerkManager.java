package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Perk} objects.
 */
@Service
@Getter
@Slf4j
public class PerkManager
{
	private final PerkLoaderService perkLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final Boolean ignoreSpecialRestrictions = false;

	/**
	 * The constructor for a {@link PerkManager} object.
	 * @param perkLoaderService A service used to load {@link Perk} objects.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public PerkManager(PerkLoaderService perkLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.applicationEventPublisher = applicationEventPublisher;
		this.perkLoaderService = perkLoaderService;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	/**
	 * A method to find a perk within a given loadout.
	 * @param perkName The name of the {@link Perk} object.
	 * @param loadout The {@link Loadout} object containing the perk.
	 * @return A {@link Perk} or null object.
	 */
	public Perk getPerkInLoadout(String perkName, Loadout loadout){
		return loadout.getPerks()
			.keySet().stream()
			.filter(key -> key.name().equalsIgnoreCase(perkName))
			.findFirst()
			.orElse(null);
	}

	//when a perk is added - it is automatically added to the effects.
	@SaveLoadout
	public void addPerk(String perkName, Loadout loadout) throws IOException
	{
		Perk perk = perkLoaderService.getPerk(perkName);
		//checks whether to ignore special point enforcement or if the player has the points available.
		if (ignoreSpecialRestrictions || hasAvailableSpecialPoints(loadout.getPerks(),
			perk, loadout.getPlayer().getSpecials()))
		{
			log.debug("Adding {} to loadout {}.", perk.name(), loadout.getLoadoutID());
			loadout.getPerks().put(perk, modifierConditionLogic.evaluateCondition(perk, loadout));

			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been added");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		} else {
			log.debug("Could not add perk {} as there were insufficent points available.", perkName);
		}
	}

	//todo - consider changing from different perkNames (as the list doesn't match)
	@SaveLoadout
	public void removePerk(String perkName, Loadout loadout) throws IOException
	{
		Perk perk = getPerkInLoadout(perkName, loadout);
		if (perk != null)
		{
			loadout.getPerks().remove(perk);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been removed");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}

	/**
	 * A method used to adjust the rank of a given perk.
	 * @param perkName The name of the perk to adjust.
	 * @param rank The new rank that will be applied to the perk.
	 * @param loadout The loadout the change will take place on.
	 */
	@SaveLoadout
	public void changePerkRank(String perkName, int rank, Loadout loadout)
	{
		Perk perk = getPerkInLoadout(perkName, loadout);
		if (perk != null && (ignoreSpecialRestrictions || hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials(), rank))) {
			perk.perkRank().setCurrentRank(rank);
		}
	}

	/**
	 * A method used to calculate if a perk can be added to the existing perk list.
	 * @param perks A {@link HashMap} of {@link Perk} objects to be used.
	 * @param perkToAdd The perk to be added.
	 * @param playerStats The amount of points available to be allocated.
	 * @param temporaryRank An optional argument which is used to determine how
	 * @return {@code true} if there are points available, otherwise {@code false}.
	 */
	public boolean hasAvailableSpecialPoints(HashMap<Perk, Boolean> perks, Perk perkToAdd, Special playerStats, int... temporaryRank){

		Specials special = perkToAdd.special();
		int availablePoints = playerStats.getSpecialValue(special);

		//uses either the perks current rank
		int requiredPoints = (temporaryRank.length > 0 && temporaryRank[0] > 0) ? perkToAdd.perkRank().getPointsCost(temporaryRank[0]) :
			perkToAdd.perkRank().getPointsCost();

		int totalUsedPoints = perks.keySet().stream()
			.filter(perk -> perk.special().equals(special) && !perk.name().equals(perkToAdd.name()))
			.mapToInt(perk -> perk.perkRank().getPointsCost())
			.sum();

		int remainingPoints = availablePoints - totalUsedPoints;

		return remainingPoints >= requiredPoints;
	}

	/**
	 * An event listener used to receive events if a
	 * @param event An event that is called when a change has been made to a {@link Loadout}'s weapon.
	 */
	@EventListener
	public void onWeaponChangedEvent(WeaponChangedEvent event)
	{
		Loadout loadout = event.getLoadout();
		for (Map.Entry<Perk, Boolean> entry : loadout.getPerks().entrySet())
		{
			Boolean newValue = modifierConditionLogic.evaluateCondition(entry.getKey(), loadout);
			if (entry.getValue() != newValue)
			{
				entry.setValue(newValue);
			}
		}
	}
}

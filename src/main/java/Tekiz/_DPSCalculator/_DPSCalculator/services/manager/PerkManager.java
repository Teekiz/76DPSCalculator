package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.SpecialChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.SpecialsChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.WeaponChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.logic.ModifierConditionLogic;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
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
	private final DataLoaderService dataLoaderService;
	private final ModifierConditionLogic modifierConditionLogic;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Setter
	private Boolean ignoreSpecialRestrictions = false;

	/**
	 * The constructor for a {@link PerkManager} object.
	 * @param dataLoaderService A service used to load {@link Perk} objects.
	 * @param modifierConditionLogic A service that is used to evaluate a {@link Modifier}'s condition logic.
	 */
	@Autowired
	public PerkManager(DataLoaderService dataLoaderService, ModifierConditionLogic modifierConditionLogic, ApplicationEventPublisher applicationEventPublisher)
	{
		this.dataLoaderService = dataLoaderService;
		this.applicationEventPublisher = applicationEventPublisher;
		this.modifierConditionLogic = modifierConditionLogic;
	}

	/**
	 * A method to find a perk within a given loadout.
	 * @param perkID The name of the {@link Perk} object.
	 * @param loadout The {@link Loadout} object containing the perk.
	 * @return A {@link Perk} or null object.
	 */
	public Perk getPerkInLoadout(String perkID, Loadout loadout){
		return loadout.getPerks()
			.keySet().stream()
			.filter(key -> key.id().equalsIgnoreCase(perkID))
			.findFirst()
			.orElse(null);
	}

	//when a perk is added - it is automatically added to the effects.
	@SaveLoadout
	public void addPerk(String perkID, Loadout loadout) throws IOException
	{
		Perk perk = dataLoaderService.loadData(perkID, Perk.class, null);
		//checks whether to ignore special point enforcement or if the player has the points available.

		if (perk == null){
			log.error("Perk loading failed for: {}", perkID);
			return;
		}

		if (ignoreSpecialRestrictions || hasAvailableSpecialPoints(loadout.getPerks(),
			perk, loadout.getPlayer().getSpecials()))
		{
			log.debug("Adding {} to loadout {}.", perk.name(), loadout.getLoadoutID());
			loadout.getPerks().put(perk, modifierConditionLogic.evaluateCondition(perk, loadout));

			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been added");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		} else {
			log.debug("Could not add perk {} as there were insufficent points available.", perkID);
		}
	}

	//todo - consider changing from different perkNames (as the list doesn't match)
	@SaveLoadout
	public void removePerk(String perkID, Loadout loadout) throws IOException
	{
		Perk perk = getPerkInLoadout(perkID, loadout);
		if (perk != null)
		{
			loadout.getPerks().remove(perk);
			ModifierChangedEvent modifierChangedEvent = new ModifierChangedEvent(perk, loadout,perk.name() + " has been removed");
			applicationEventPublisher.publishEvent(modifierChangedEvent);
		}
	}

	/**
	 * A method used to remove perks from a loadout while the total points used exceeds the points available.
	 * @param loadout The loadout to remove the perks from.
	 * @param special The special that the perks will be used from.
	 * @param availablePoints The amount of points available to be used.
	 * @throws IOException
	 */
	private void removeExcessPerksForSpecial(Loadout loadout, Specials special, int availablePoints) throws IOException
	{
		List<Perk> perksUsed = loadout.getPerks().keySet().stream()
			.filter(perk -> perk.special().equals(special))
			.toList();
		for (Perk perk : perksUsed.reversed()){
			log.debug("Not enough points available. Removing {}.", perk.name());
			removePerk(perk.id(), loadout);
			int pointsUsed = getPerkPointsUsed(special, loadout.getPerks());
			if (availablePoints >= pointsUsed) {
				break;
			}
		}
	}

	/**
	 * A method used to adjust the rank of a given perk.
	 * @param perkID The name of the perk to adjust.
	 * @param rank The new rank that will be applied to the perk.
	 * @param loadout The loadout the change will take place on.
	 */
	@SaveLoadout
	public void changePerkRank(String perkID, int rank, Loadout loadout)
	{
		Perk perk = getPerkInLoadout(perkID, loadout);
		if (perk != null && (ignoreSpecialRestrictions || hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials(), rank))) {
			perk.perkRank().setCurrentRank(rank);
		}
	}

	/**
	 * A method used to calculate if a perk can be added to the existing perk list.
	 * @param perks A {@link HashMap} of {@link Perk} objects to be used.
	 * @param perkToAdd The perk to be added.
	 * @param playerStats The amount of points available to be allocated.
	 * @param temporaryRank An optional argument which is used to determine what the rank of the card is. (if not included, the rank is set to the perks {@code currentRank}.)
	 * @return {@code true} if there are points available, otherwise {@code false}.
	 */
	public boolean hasAvailableSpecialPoints(HashMap<Perk, Boolean> perks, Perk perkToAdd, Special playerStats, int... temporaryRank){

		Specials special = perkToAdd.special();
		int availablePoints = playerStats.getSpecialValue(special);

		//uses either the perks current rank
		int requiredPoints = (temporaryRank.length > 0 && temporaryRank[0] > 0) ? perkToAdd.perkRank().getPointsCost(temporaryRank[0]) :
			perkToAdd.perkRank().getPointsCost();
		int totalUsedPoints = getPerkPointsUsed(special, perks, perkToAdd.name());
		int remainingPoints = availablePoints - totalUsedPoints;
		return remainingPoints >= requiredPoints;
	}

	/**
	 * A method used to determine how many points a SPECIAL stat currently has allocated.
	 * @param special The special stat used to find associated cards.
	 * @param perks A {@link HashMap} of {@link Perk} objects to be used.
	 * @param perkToAddName An optional argument to filter out matching cards.
	 * @return The {@link Integer} the total allocated special perk points.
	 */
	private int getPerkPointsUsed(Specials special, HashMap<Perk, Boolean> perks, String... perkToAddName){
		String filter = perkToAddName.length > 0 ? perkToAddName[0] : "";

		return perks.keySet().stream()
			.filter(perk -> perk.special().equals(special) && (!perk.name().equals(filter)))
			.mapToInt(perk -> perk.perkRank().getPointsCost())
			.sum();
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

	/**
	 * An event listener used to receive events if a players special stat changes. It checks to make sure that the player has
	 * enough special points left to fit in each card. Otherwise, it removes one perk card in the perk list until there is enough room.
	 * @param event An event that is called when a change has been made to a {@link Loadout}'s player special stat.
	 */
	@EventListener
	public void onSpecialChangedEvent(SpecialChangedEvent event) throws IOException
	{
		int availablePoints = event.getAvailablePoints();
		int pointsUsed = getPerkPointsUsed(event.getSpecial(), event.getLoadout().getPerks());

		//if there is not enough available points left in the changed special slot or this check should be bypassed.
		if (ignoreSpecialRestrictions || availablePoints < pointsUsed){
			removeExcessPerksForSpecial(event.getLoadout(), event.getSpecial(), availablePoints);
		}
	}

	/**
	 * An event listener used to receive events if all of a players special stats change. It checks to make sure that the player has
	 * enough special points left to fit in each card. Otherwise, it removes one perk card in the perk list until there is enough room.
	 * @param event An event that is called when a change has been made to a {@link Loadout}'s player special stat.
	 */
	@EventListener
	public void onSpecialsChangedEvent(SpecialsChangedEvent event) throws IOException
	{
		Loadout loadout = event.getLoadout();
		Special specials = loadout.getPlayer().getSpecials();
		HashMap<Perk, Boolean> perks = loadout.getPerks();

		if (!ignoreSpecialRestrictions){
			for (Specials special : Specials.values()) {
				int availablePoints = specials.getSpecialValue(special);
				int pointsUsed = getPerkPointsUsed(special, perks);
				if (availablePoints < pointsUsed) {
					removeExcessPerksForSpecial(loadout, special, availablePoints);
				}
			}
		}
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.MinorStatCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.SpecialChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.services.events.SpecialsChangedEvent;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Player} objects.
 */
@Service
@Getter
public class PlayerManager
{
	private final MinorStatCalculationService minorStatCalculationService;
	private final SpecialBonusCalculationService specialBonusCalculationService;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * The constructor of a {@link PlayerManager} object.
	 */
	@Autowired
	public PlayerManager(MinorStatCalculationService minorStatCalculationService, SpecialBonusCalculationService specialBonusCalculationService, ApplicationEventPublisher applicationEventPublisher)
	{
		this.minorStatCalculationService = minorStatCalculationService;
		this.specialBonusCalculationService = specialBonusCalculationService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	//todo - possibly delete
	public Player getPlayer(Loadout loadout)
	{
		return loadout.getPlayer();
	}

	/**
	 * A method used to change a single special stat.
	 * @param loadout The loadout where the stat will be changed.
	 * @param special The type of stat that will be changed.
	 * @param value The value the special will be changed to.
	 */
	@SaveLoadout
	public void setSpecial(Loadout loadout, Specials special, int value)
	{
		Special playerSpecials = loadout.getPlayer().getSpecials();
		playerSpecials.setSpecial(special, value);
		SpecialChangedEvent specialChangedEvent = new SpecialChangedEvent(this, special,
			playerSpecials.getSpecialValue(special), loadout);
		applicationEventPublisher.publishEvent(specialChangedEvent);
	}

	/**
	 * A method used to change all or multiple special stats.
	 * @param loadout The loadout where the stats will be changed.
	 * @param newSpecials A {@link SpecialDTO} of the new values that will be modified.
	 */
	@SaveLoadout
	public void setSpecialsFromDTO(Loadout loadout, SpecialDTO newSpecials)
	{
		Special playerSpecials = loadout.getPlayer().getSpecials();
		playerSpecials.setAllSpecials(newSpecials.getStrength(), newSpecials.getPerception(), newSpecials.getEndurance(),
			newSpecials.getCharisma(), newSpecials.getIntelligence(), newSpecials.getAgility(), newSpecials.getLuck());
		SpecialsChangedEvent specialsChangedEvent = new SpecialsChangedEvent(this, loadout);
		applicationEventPublisher.publishEvent(specialsChangedEvent);
	}

	@EventListener
	public void onModifierChangedEvent(ModifierChangedEvent event)
	{
		Loadout loadout = event.getLoadout();
		int enduranceBonus = specialBonusCalculationService.getSpecialBonus(Specials.ENDURANCE, loadout);
		int agilityBonus = specialBonusCalculationService.getSpecialBonus(Specials.AGILITY, loadout);
		double healthBonus = minorStatCalculationService.calculateHealthPointBonuses(loadout);

		loadout.getPlayer().setMaxHP(enduranceBonus, healthBonus);
		loadout.getPlayer().setMaxAP(agilityBonus);
	}
}

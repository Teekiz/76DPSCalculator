package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.aspect.SaveLoadout;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MinorStatCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * The constructor of a {@link PlayerManager} object.
	 */
	@Autowired
	public PlayerManager(MinorStatCalculationService minorStatCalculationService, SpecialBonusCalculationService specialBonusCalculationService)
	{
		this.minorStatCalculationService = minorStatCalculationService;
		this.specialBonusCalculationService = specialBonusCalculationService;
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
	public void modifySpecial(Loadout loadout, Specials special, int value)
	{
		Special playerSpecials = loadout.getPlayer().getSpecials();
		playerSpecials.modifySpecial(special, value);
	}

	/**
	 * A method used to change all or multiple special stats.
	 * @param loadout The loadout where the stats will be changed.
	 * @param newSpecials A {@link SpecialDTO} of the new values that will be modified.
	 */
	@SaveLoadout
	public void modifySpecialsFromDTO(Loadout loadout, SpecialDTO newSpecials)
	{
		Special playerSpecials = loadout.getPlayer().getSpecials();
		playerSpecials.modifyAllSpecials(newSpecials.getStrength(), newSpecials.getPerception(), newSpecials.getEndurance(),
			newSpecials.getCharisma(), newSpecials.getIntelligence(), newSpecials.getAgility(), newSpecials.getLuck());

	}

	@EventListener
	public void onModifierChangedEvent(ModifierChangedEvent event)
	{
		Loadout loadout = event.getLoadout();
		int enduranceBonus = specialBonusCalculationService.getSpecialBonus(Specials.ENDURANCE, loadout);
		double healthBonus = minorStatCalculationService.calculateHealthPointBonuses(loadout);
		loadout.getPlayer().setMaxHP(enduranceBonus, healthBonus);
	}
}

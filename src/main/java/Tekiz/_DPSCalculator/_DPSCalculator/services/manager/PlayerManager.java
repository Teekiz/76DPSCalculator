package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
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

	public Player getPlayer(Loadout loadout)
	{
		return loadout.getPlayer();
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

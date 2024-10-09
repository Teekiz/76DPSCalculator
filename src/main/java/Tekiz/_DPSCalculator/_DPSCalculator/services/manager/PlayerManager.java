package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MinorStatCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
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
	@Lookup
	protected LoadoutManager getLoadoutManager()
	{
		return null;
	}
	public Player getPlayer()
	{
		return getLoadoutManager().getActiveLoadout().getPlayer();
	}
	@EventListener
	public void onModifierChangedEvent(ModifierChangedEvent event)
	{
		int enduranceBonus = specialBonusCalculationService.getSpecialBonus(Specials.ENDURANCE);
		double healthBonus = minorStatCalculationService.calculateHealthPointBonuses();
		getPlayer().setMaxHP(enduranceBonus, healthBonus);
	}
}

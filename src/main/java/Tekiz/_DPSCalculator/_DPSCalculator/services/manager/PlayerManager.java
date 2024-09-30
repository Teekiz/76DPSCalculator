package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.MinorStatCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ModifierChangedEvent;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Player} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class PlayerManager implements LoadoutScopeClearable
{
	private Player player;
	private final MinorStatCalculationService minorStatCalculationService;
	private final SpecialBonusCalculationService specialBonusCalculationService;

	/**
	 * The constructor of a {@link PlayerManager} object.
	 */
	@Autowired
	public PlayerManager(MinorStatCalculationService minorStatCalculationService, SpecialBonusCalculationService specialBonusCalculationService)
	{
		this.player = new Player();
		this.minorStatCalculationService = minorStatCalculationService;
		this.specialBonusCalculationService = specialBonusCalculationService;
	}

	/**
	 * A method used during the cleanup of this service.
	 */

	@EventListener
	public void onModifierChangedEvent(ModifierChangedEvent event)
	{
		int enduranceBonus = specialBonusCalculationService.getSpecialBonus(Specials.ENDURANCE);
		double healthBonus = minorStatCalculationService.calculateHealthPointBonuses();
		player.setMaxHP(enduranceBonus, healthBonus);
	}

	@PreDestroy
	public void clear() {
		player = null;
	}

}

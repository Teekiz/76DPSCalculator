package Tekiz._DPSCalculator._DPSCalculator.model.player;

import lombok.Data;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.SpecialBonusCalculationService;

/** Represents a player including the players stats (SPECIAL, HP) and state (actions).*/
@Data
public class Player
{
	/** Represents the players base special stats. Each stat cannot have a value lower than 1 or higher than 15. Boosts to these stats are handled through
	 * {@link SpecialBonusCalculationService}.*/
	private Special specials;
	private int level;

	private double maxHP;
	private double currentHP;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	/**
	 * The constructor for a {@link Player} object.
	 */
	public Player()
	{
		this.level = 1;
		this.specials = new Special(1, 1, 1, 1, 1, 1, 1);

		setMaxHP();
		setCurrentHP(maxHP);
	}

	/** Sets the maximum health points a player can have. This is calculated as 245 + 5 * Endurance. */
	public void setMaxHP()
	{
		maxHP = 245 + 5 * specials.getEndurance();
	}

	/** Sets the current health points a player has. This cannot be higher than the maximum HP. */
	public void setCurrentHP(double hp)
	{
		currentHP = Math.min(maxHP, hp);
	}

	/**
	 * Gets the percentage of health remaining. Useful for mutations such as Adrenal Reaction or effects such as Bloodied.
	 * @return {@link Double} healthPercentage.
	 * */
	public double getHealthPercentage()
	{
		return (currentHP / maxHP) * 100;
	}
}

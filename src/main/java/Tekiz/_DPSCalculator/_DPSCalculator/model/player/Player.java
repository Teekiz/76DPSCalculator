package Tekiz._DPSCalculator._DPSCalculator.model.player;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.AttackType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.PlayerBonuses.SpecialBonusCalculationService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** Represents a player including the players stats (SPECIAL, HP) and state (actions).*/
@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Player implements Serializable
{
	/** Represents the players base special stats. Each stat cannot have a value lower than 1 or higher than 15. Boosts to these stats are handled through
	 * {@link SpecialBonusCalculationService}.
	 */
	@JsonProperty("specials")
	private final Special specials;
	@JsonProperty("maxHP")
	private double maxHP;
	@JsonProperty("currentHP")
	private double currentHP;
	@JsonProperty("maxAP")
	private double maxAP;

	@Setter
	@JsonProperty("level")
	private int level;

	@Setter
	@JsonProperty("isSneaking")
	private boolean isSneaking = false;
	@Setter
	@JsonProperty("attackType")
	private AttackType attackType = AttackType.STANDARD;

	/**
	 * The constructor for a {@link Player} object.
	 */
	@JsonIgnore
	public Player()
	{
		this.level = 1;
		this.specials = new Special(1, 1, 1, 1, 1, 1, 1,0,0,0,0,0,0,0);

		setMaxAP(0);
		setMaxHP(0);
		setCurrentHP(maxHP);
	}

	/** Sets the maximum health points a player can have. This is calculated as 245 + 5 * Endurance. */
	public void setMaxHP(double bonusHP)
	{
		maxHP = 245 + 5 * (specials.getSpecialValue(Specials.ENDURANCE, true)) + bonusHP;
		setCurrentHP(currentHP);
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
	@JsonIgnore
	public double getHealthPercentage()
	{
		return (currentHP / maxHP) * 100;
	}

	/**
	 * Sets the maximum AP a player has.
	 */
	public void setMaxAP(int bonusAP)
	{
		maxAP = (60 + 10 * specials.getSpecialValue(Specials.AGILITY, true)) + bonusAP;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.player;

import lombok.Data;

@Data
public class Player
{
	private Special specials;
	private int level;

	private double maxHP;
	private double currentHP;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	public Player()
	{
		this.level = 1;
		this.specials = new Special(1, 1, 1, 1, 1, 1, 1);

		setMaxHP();
		setCurrentHP(maxHP);
	}

	public void setMaxHP()
	{
		maxHP = 245 + 5 * specials.getEndurance();
	}

	public void setCurrentHP(double hp)
	{
		currentHP = Math.min(maxHP, hp);
	}

	public double getHealthPercentage()
	{
		return (currentHP / maxHP) * 100;
	}

}

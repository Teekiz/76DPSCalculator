package Tekiz._DPSCalculator._DPSCalculator.model.player;

import lombok.Data;

@Data
public class Player
{
	private Special specials;
	private int level;
	//todo
	private int maxHP;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	public Player()
	{
		this.level = 1;
		this.specials = new Special(1, 1, 1, 1, 1, 1, 1, 1, 15);
	}

	/*
	public void clear()
	{
		if (this.specials != null)
		{
			this.specials = null;
		}
	}

	 */

	//used to update the specials values based on enum provided. checks to make sure that specials cannot go over 100 or below 1.

	//todo - implement the specifics for armour slots, consumables and specials

}

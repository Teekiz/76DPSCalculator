package Tekiz._DPSCalculator._DPSCalculator.model.player;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Player
{
	private Special specials;
	private int level;
	//todo
	private int maxHP;

	//there can be many food effects but only one of each alcohol and chem.
	private Set<Perk> perks;
	private Set<Consumable> consumables;

	private boolean isAiming = false;
	private boolean isSneaking = false;

	public Player()
	{
		this.level = 1;
		this.specials = new Special(1, 1, 1, 1, 1, 1, 1, 1, 15);
		this.perks = new HashSet<>();
		this.consumables = new HashSet<>();
	}

	//used to update the specials values based on enum provided. checks to make sure that specials cannot go over 100 or below 1.

	//todo - implement the specifics for armour slots, consumables and specials

	public void addConsumable(Consumable consumable)
	{
		consumables.add(consumable);
	}
	public void removeConsumable(Consumable consumable)
	{
		consumables.remove(consumable);
	}
}

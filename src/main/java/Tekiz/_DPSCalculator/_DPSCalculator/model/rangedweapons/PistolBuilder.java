package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import java.util.HashMap;

public class PistolBuilder extends RangedBuilder<PistolBuilder>
{

	private Receiver receiver;
	public PistolBuilder(String weaponName, HashMap<Integer, Double> weaponDamageByLevel, int apCost)
	{
		super(weaponName, weaponDamageByLevel, apCost);
	}

	public PistolBuilder setReceiver(Receiver receiver)
	{
		this.receiver = receiver;
		return self();
	}

	@Override
	protected PistolBuilder self()
	{
		return this;
	}

	@Override
	public Pistol build()
	{
		return new Pistol(weaponName, weaponDamageByLevel, apCost, magazineSize, fireRate,
			range, accuracy, projectileCount, criticalBonus, rangedPenalty, reloadTime, attackSpeed, attackDelay, receiver);
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;

public class PistolBuilder extends RangedBuilder<PistolBuilder>
{

	private Receiver receiver;
	public PistolBuilder(String weaponName, int[] weaponLevels, int[] weaponDamageValues, int apCost)
	{
		super(weaponName, weaponLevels, weaponDamageValues, apCost);
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
		return new Pistol(weaponName, weaponLevels, weaponDamageValues, apCost, magazineSize, fireRate,
			range, accuracy, projectileCount, criticalBonus, rangedPenalty, reloadTime, attackSpeed, attackDelay, receiver);
	}
}

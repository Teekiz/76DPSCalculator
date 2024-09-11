package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;
import lombok.Getter;

@Getter
public class Receiver extends RangedMod
{
	//todo - change receivertype and ammo override type
	private final ReceiverType receiverType;
	private final double damageChange;
	private final int damageCriticalMultiplier;
	private final int fireRateChange;
	private final int accuracyChange;
	private final double apChange;
	private final boolean isPrime;
	private final String ammoOverrideType;

	public Receiver(String name, ReceiverType receiverType,
					double damageChange, int damageCriticalMultiplier, int fireRateChange,
					int accuracyChange, double apChange, boolean isPrime, String ammoOverrideType)
	{
		super(name);
		this.receiverType = receiverType;
		this.damageChange = damageChange;
		this.damageCriticalMultiplier = damageCriticalMultiplier;
		this.fireRateChange = fireRateChange;
		this.accuracyChange = accuracyChange;
		this.apChange = apChange;
		this.isPrime = isPrime;
		this.ammoOverrideType = ammoOverrideType;
	}
}

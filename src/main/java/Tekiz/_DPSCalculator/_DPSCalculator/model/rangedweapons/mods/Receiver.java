package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter @Getter @RequiredArgsConstructor
public class Receiver
{
	//todo - change receivertype and ammo override type
	private String name;
	private String receiverType;
	private double damageChange;
	private int damageCriticalMultiplier;
	private int fireRateChange;
	private int accuracyChange;
	private double apChange;
	private boolean isPrime;
	private String ammoOverrideType;
}

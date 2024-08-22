package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Receivers
{
	AUTOMATIC(ReceiverType.AUTOMATIC, 0.90, 0,32, 0, 0.50, false, false, null),
	CALIBRATED(ReceiverType.SEMIAUTOMATIC, 0, 100,0,1, 0, false, false, null),
	FIERCE(ReceiverType.SEMIAUTOMATIC, 0, 50, 9, 0, 0.05, false, false, null);

	private final ReceiverType receiverType;
	private final double damageChange;
	private final int critialDamage;
	private final int fireRateChange;
	private final int accuracyChange;
	private final double apChange;
	private final boolean isPrime;
	private final boolean overrideAmmo;
	private final Ammo overrideAmmoType;

}

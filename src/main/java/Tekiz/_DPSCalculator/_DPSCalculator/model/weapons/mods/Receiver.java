package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;
import lombok.Getter;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.calculation.BonusDamageService;

/**
 * Represents a receiver modification for a ranged weapon.
 */
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

	/**
	 * The constructor for a receiver.
	 * @param name The name of the receiver. The user will be able to see the given value.
	 * @param receiverType The type of receiver. This is used for various {@link Modifier} conditions.
	 * @param damageChange The additive {@link Double} value that will be applied to the {@link BonusDamageService}.
	 * @param damageCriticalMultiplier The {@link Integer} value that will be made to the critical damage.
	 * @param fireRateChange The modification made to the weapons base rate of fire.
	 * @param accuracyChange The modification made to the weapons base accuracy.
	 * @param apChange The modification made to the weapons base action points usage.
	 * @param isPrime Determines if the receiver is a prime receiver or not.
	 * @param ammoOverrideType The ammo the weapon uses if the receiver changes from the base value.
	 */
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

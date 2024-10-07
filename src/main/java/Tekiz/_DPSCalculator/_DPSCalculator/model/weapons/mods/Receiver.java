package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a receiver modification for a ranged weapon.
 */
@Value
@Jacksonized @SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Receiver extends RangedMod
{
	//todo - change receivertype and ammo override type
	@JsonProperty("receiverType")
	ReceiverType receiverType;
	@JsonProperty("damageChange")
	double damageChange;
	@JsonProperty("damageCriticalMultiplier")
	int damageCriticalMultiplier;
	@JsonProperty("fireRateChange")
	int fireRateChange;
	@JsonProperty("accuracyChange")
	int accuracyChange;
	@JsonProperty("apChange")
	double apChange;
	@JsonProperty("prime")
	boolean isPrime;
	@JsonProperty("ammoOverrideType")
	String ammoOverrideType;
}

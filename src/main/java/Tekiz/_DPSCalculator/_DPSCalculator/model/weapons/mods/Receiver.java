package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Represents a receiver modification for a ranged weapon.
 */
@Getter
public class Receiver extends RangedMod
{
	//todo - change receivertype and ammo override type
	@JsonProperty("receiverType")
	private final ReceiverType receiverType;
	@JsonProperty("damageChange")
	private final double damageChange;
	@JsonProperty("damageCriticalMultiplier")
	private final int damageCriticalMultiplier;
	@JsonProperty("fireRateChange")
	private final int fireRateChange;
	@JsonProperty("accuracyChange")
	private final int accuracyChange;
	@JsonProperty("apChange")
	private final double apChange;
	@JsonProperty("prime")
	private final boolean isPrime;
	@JsonProperty("ammoOverrideType")
	private final String ammoOverrideType;
	@JsonCreator
	public Receiver(@JsonProperty("name") String name, @JsonProperty("receiverType") ReceiverType receiverType,
					@JsonProperty("damageChange") double damageChange, @JsonProperty("damageCriticalMultiplier") int damageCriticalMultiplier,
					@JsonProperty("fireRateChange") int fireRateChange, @JsonProperty("accuracyChange") int accuracyChange,
					@JsonProperty("apChange") double apChange, @JsonProperty("prime") boolean isPrime,
					@JsonProperty("ammoOverrideType") String ammoOverrideType)
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

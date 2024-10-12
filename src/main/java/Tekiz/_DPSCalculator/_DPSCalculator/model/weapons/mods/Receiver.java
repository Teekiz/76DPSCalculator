package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a receiver modification for a ranged weapon.
 *
 * @param receiverType todo - change receivertype and ammo override type
 */
public record Receiver(@JsonProperty("name") String name, @JsonProperty("receiverType") ReceiverType receiverType,
					   @JsonProperty("damageChange") double damageChange,
					   @JsonProperty("damageCriticalMultiplier") int damageCriticalMultiplier,
					   @JsonProperty("fireRateChange") int fireRateChange,
					   @JsonProperty("accuracyChange") int accuracyChange, @JsonProperty("apChange") double apChange,
					   @JsonProperty("prime") boolean isPrime,
					   @JsonProperty("ammoOverrideType") String ammoOverrideType) implements RangedMod {}

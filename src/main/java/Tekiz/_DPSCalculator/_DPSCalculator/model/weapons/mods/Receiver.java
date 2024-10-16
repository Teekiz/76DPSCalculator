package Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ReceiverType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a receiver modification for a ranged weapon.
 * @param name 						The name of the receiver.
 * @param receiverType				The fire rate of the receiver.
 * @param damageChange				The damage the base weapon will gain or lose.
 * @param damageCriticalMultiplier	The damage multiplier that the receiver adds (base is 0).
 * @param fireRateChange			The change made to the speed the weapon attacks at.
 * @param accuracyChange			How much accuracy the weapon will gain or lose.
 * @param apChange					The amount the weapon will cost to use in VATS.
 * @param isPrime					Used to denote if the weapon should apply extra damage to scorched enemies.
 * @param ammoOverrideType			Used if the receiver changes the ammunition the weapon uses.
 */
//todo - change receivertype and ammo override type
public record Receiver(@JsonProperty("name") String name,
					   @JsonProperty("receiverType") ReceiverType receiverType,
					   @JsonProperty("damageChange") double damageChange,
					   @JsonProperty("damageCriticalMultiplier") int damageCriticalMultiplier,
					   @JsonProperty("fireRateChange") int fireRateChange,
					   @JsonProperty("accuracyChange") int accuracyChange,
					   @JsonProperty("apChange") double apChange,
					   @JsonProperty("prime") boolean isPrime,
					   @JsonProperty("ammoOverrideType") String ammoOverrideType) implements RangedMod {}

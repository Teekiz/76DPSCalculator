package Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;


/**
 * Captures the damage that a weapon or effect applies.
 * @param damageType The type of damage provided.
 * @param damage The amount of damage provided.
 * @param overTime The time the damage lasts for (in seconds). For example, a value of 60 and 3.0 would deal 20 damage per second.
 */
public record WeaponDamage(DamageType damageType, double damage, double overTime)
{}

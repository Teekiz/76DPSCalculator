package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import lombok.Getter;

@Getter
public enum ModifierTypes
{
	DAMAGE_ADDITIVE(Double.class),
	DAMAGE_MULTIPLICATIVE(Double.class),
	PENETRATION(Double.class),
	ATTACKSPEED(Double.class),
	RELOADSPEED(Double.class),
	ACCURACY(Double.class),
	RANGE(Double.class),
	SNEAK_DAMAGE(Double.class),
	CRITICAL(Double.class),
	CRITICAL_CONSUMPTION(Integer.class),
	HEALTH(Double.class),
	SPECIAL_STRENGTH(Integer.class),
	SPECIAL_PERCEPTION(Integer.class),
	SPECIAL_ENDURANCE(Integer.class),
	SPECIAL_CHARISMA(Integer.class),
	SPECIAL_INTELLIGENCE(Integer.class),
	SPECIAL_AGILITY(Integer.class),
	SPECIAL_LUCK(Integer.class),
	MAX_AP(Integer.class),
	AP_REGEN(Integer.class),
	AP_COST(Double.class),

	//MODIFICATIONS
	FIRE_RATE(Integer.class),
	ACCURACY_ADDITIVE(Integer.class),
	ACCURACY_HIPFIRE(Double.class),
	ACCURACY_SIGHTED(Double.class),
	AIM_SPEED(Double.class),
	RECOIL(Double.class),
	SCORCHED_DAMAGE(Double.class),

	//if multiple contexts are required, could change from String to List.
	ADDITIONAL_CONTEXT_REQUIRED(String.class),
	SCRIPT(String.class),
	//This is used if an effect affects other modifiers (therefore requiring priority)
	PRIORITY_AFFECTS_MODIFIERS(ModifierBoost.class),
	//This is used when a modification changes all the damage types.
	CHANGES_WEAPON_DAMAGE(WeaponDamage.class),
	//This is used when an existing damage type is used, but it is added.
	ADDS_WEAPON_DAMAGE(WeaponDamage.class),
	ERROR_TYPE(String.class);

	private final Class<?> valueType;

	ModifierTypes(Class<?> valueType)
	{
		this.valueType = valueType;
	}
}

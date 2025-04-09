package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.statusEffects.StatusEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import lombok.Getter;

@Getter
public enum ModifierTypes
{
	DAMAGE_ADDITIVE(Double.class, "Bonus damage"),
	DAMAGE_MULTIPLICATIVE(Double.class, "Damage multiplier"),
	PENETRATION(Double.class, "Armour penetration."),
	ATTACKSPEED(Double.class, "Attack speed"),
	RELOADSPEED(Double.class, "Reload speed"),
	ACCURACY(Integer.class, "Accuracy change"),
	RANGE(Double.class, "Range"),
	SNEAK_DAMAGE(Double.class, "Sneak damage"),
	CRITICAL(Double.class, "Critical damage"),
	CRITICAL_CONSUMPTION(Integer.class, "Critical consumption", true),
	HEALTH(Double.class, "Maximum health points"),
	SPECIAL_STRENGTH(Integer.class, "Strength"),
	SPECIAL_PERCEPTION(Integer.class, "Perception"),
	SPECIAL_ENDURANCE(Integer.class, "Endurance"),
	SPECIAL_CHARISMA(Integer.class, "Charisma"),
	SPECIAL_INTELLIGENCE(Integer.class, "Intelligence"),
	SPECIAL_AGILITY(Integer.class, "Agility"),
	SPECIAL_LUCK(Integer.class, "Luck"),
	MAX_AP(Integer.class, "Maximum action points"),
	AP_REGEN(Integer.class, "Action point regeneration rate"),
	AP_COST(Double.class, "Action point cost"),

	//MODIFICATIONS
	FIRE_RATE(Integer.class, "Fire rate"),
	ACCURACY_ADDITIVE(Integer.class, "Accuracy"),
	ACCURACY_HIPFIRE(Double.class, "Hip fire accuracy"),
	ACCURACY_SIGHTED(Double.class, "Sighted accuracy"),
	AIM_SPEED(Double.class, "Aim speed"),
	RECOIL(Double.class, "Recoil change"),
	SCORCHED_DAMAGE(Double.class, "Scorched damage"),

	//if multiple contexts are required, could change from String to List.
	ADDITIONAL_CONTEXT_REQUIRED(String.class),
	SCRIPT(String.class),
	//This is used if an effect affects other modifiers (therefore requiring priority)
	PRIORITY_AFFECTS_MODIFIERS(ModifierBoost.class),
	//This is used when a modification changes all the damage types.
	CHANGES_WEAPON_DAMAGE(WeaponDamage.class),
	//This is used when an existing damage type is used, but it is added.
	ADDS_WEAPON_DAMAGE(WeaponDamage.class),
	//This is used when more details around the status effect applied are required.
	APPLIES_STATUS_EFFECT(StatusEffect.class),
	//if this changes when the special value changes
	BASED_ON_SPECIAL(null),
	//if this doesn't occur everytime.
	CHANCE_TO_OCCUR(null),
	ERROR_TYPE(String.class);


	private final Class<?> valueType;
	private final String displayName;
	private final Boolean inverseValue;

	//if only the value type and display name is important
	ModifierTypes(Class<?> valueType, String displayName)
	{
		this.valueType = valueType;
		this.displayName = displayName;
		this.inverseValue = false;
	}

	//if the value should be displayed inversely (i.e. +20% becomes -20%).
	ModifierTypes(Class<?> valueType, String displayName, Boolean inverseValue)
	{
		this.valueType = valueType;
		this.displayName = displayName;
		this.inverseValue = inverseValue;
	}

	//if the type is only important (i.e. the modifier is for internal use only)
	ModifierTypes(Class<?> valueType)
	{
		this.valueType = valueType;
		this.displayName = null;
		this.inverseValue = false;
	}
}

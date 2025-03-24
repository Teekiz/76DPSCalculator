package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

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
	//if multiple contexts are required, could change from String to List.
	ADDITIONAL_CONTEXT_REQUIRED(String.class),
	SCRIPT(String.class),
	//This is used if an effect affects other modifiers (therefore requiring priority)
	PRIORITY_AFFECTS_MODIFIERS(ModifierBoost.class),
	ERROR_TYPE(String.class);

	private final Class<?> valueType;

	ModifierTypes(Class<?> valueType)
	{
		this.valueType = valueType;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.BonusTypes;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class Modifiers
{
	private final SpecialModifiers specialModifier;
	private final MiscModifiers miscModifiers;
	private final HashMap<Object, SourceData> modifierData;

	public Modifiers()
	{
		specialModifier = new SpecialModifiers();
		miscModifiers = new MiscModifiers();
		modifierData = new HashMap<>();
	}

	public void addModifier(Object sourceObject, BonusTypes bonusType, double bonus)
	{
		modifierData.put(sourceObject, new SourceData(bonusType, bonus));

		switch (bonusType)
		{
			case DAMAGE_ADDITIVE -> miscModifiers.addAdditiveWeaponDamageBonus(bonus);
			case DAMAGE_MULTIPLICATIVE -> miscModifiers.addMultiplicativeWeaponDamageBonus(bonus);
			case PENETRATION_PHYSICAL -> miscModifiers.addPenetrationPhysicalBonus(bonus);
			case PENETRATION_ENERGY -> miscModifiers.addPenetrationEnergyBonus(bonus);
			case PENETRATION_RADIATION -> miscModifiers.addPenetrationRadiationBonus(bonus);
			case PENETRATION_POISON -> miscModifiers.addPenetrationPoisonBonus(bonus);
			case ATTACKSPEED -> miscModifiers.addAttackSpeedBonus(bonus);
			case RELOADSPEED -> miscModifiers.addReloadSpeedBonus(bonus);
			case ACCURACY -> miscModifiers.addAccuracyBonus(bonus);
			case RANGE ->  miscModifiers.addRangeBonus(bonus);
			case SNEAK -> miscModifiers.addSneakBonus(bonus);
			case CRITICAL -> miscModifiers.addCriticalBonus(bonus);
			case SPECIAL_STRENGTH, SPECIAL_PERCEPTION, SPECIAL_ENDURANCE,
				SPECIAL_CHARISMA, SPECIAL_INTELLIGENCE, SPECIAL_AGILITY,
				SPECIAL_LUCK -> specialModifier.addSpecialBonus(bonusType, bonus);
		}
	}

	//this is used to remove bonuses if the boost should no longer be applied (e.g. the players weapon change so a perk does not apply)
	public void removeModifier(Object sourceObject)
	{
		SourceData sourceData = modifierData.get(sourceObject);

		if (sourceData == null) return;

		switch (sourceData.getBonusTypes())
		{
			case DAMAGE_ADDITIVE -> miscModifiers.removeAdditiveWeaponDamageBonus(sourceData.getValue());
			case DAMAGE_MULTIPLICATIVE -> miscModifiers.removeMultiplicativeWeaponDamageBonus(sourceData.getValue());
			case PENETRATION_PHYSICAL -> miscModifiers.removePenetrationPhysicalBonus(sourceData.getValue());
			case PENETRATION_ENERGY -> miscModifiers.removePenetrationEnergyBonus(sourceData.getValue());
			case PENETRATION_RADIATION -> miscModifiers.removePenetrationRadiationBonus(sourceData.getValue());
			case PENETRATION_POISON -> miscModifiers.removePenetrationPoisonBonus(sourceData.getValue());
			case ATTACKSPEED -> miscModifiers.removeAttackSpeedBonus(sourceData.getValue());
			case RELOADSPEED -> miscModifiers.removeReloadSpeedBonus(sourceData.getValue());
			case ACCURACY -> miscModifiers.removeAccuracyBonus(sourceData.getValue());
			case RANGE ->  miscModifiers.removeRangeBonus(sourceData.getValue());
			case SNEAK -> miscModifiers.removeSneakBonus(sourceData.getValue());
			case CRITICAL -> miscModifiers.removeCriticalBonus(sourceData.getValue());
			case SPECIAL_STRENGTH, SPECIAL_PERCEPTION, SPECIAL_ENDURANCE,
				SPECIAL_CHARISMA, SPECIAL_INTELLIGENCE, SPECIAL_AGILITY,
				SPECIAL_LUCK -> specialModifier.removeSpecialBonus(sourceData.getBonusTypes(), sourceData.getValue());
		}
	}
}

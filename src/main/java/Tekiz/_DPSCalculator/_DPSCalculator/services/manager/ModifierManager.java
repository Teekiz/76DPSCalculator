package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.BonusTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.MiscModifiers;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.ModifierData;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.SpecialModifiers;
import Tekiz._DPSCalculator._DPSCalculator.services.events.ConditionChangedEvent;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Getter
@Service
@Scope(scopeName = "loadout")
public class ModifierManager implements LoadoutScopeClearable
{

	/*
		Order of operations:
		weapons, enemy and environment
		perks affect mutations, consumables and stats
		mutations affect consumables and stats
		consumables affect stats
		legendary effects
		stats last
	 */

	private final SpecialModifiers specialModifier;
	private final MiscModifiers miscModifiers;
	private HashMap<String, ModifierData> modifierData;
	public ModifierManager()
	{
		this.specialModifier = new SpecialModifiers();
		this.miscModifiers = new MiscModifiers();
		this.modifierData = new HashMap<>();
	}

	public void addModifier(String sourceObjectName, BonusTypes bonusType, double bonus)
	{
		if (addToModifierData(sourceObjectName, bonusType, bonus))
		{
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
	}

	public boolean addToModifierData(String sourceObjectName, BonusTypes bonusType, double bonus)
	{
		//if the modifiers already contain a source object, and the bonus is already added, return false, otherwise create a new bonus modifier.
		if (modifierData.get(sourceObjectName) != null)
		{
			return modifierData.get(sourceObjectName).addModifier(bonusType, bonus);
		}
		else
		{
			modifierData.put(sourceObjectName, new ModifierData());
			modifierData.get(sourceObjectName).addModifier(bonusType, bonus);
			return true;
		}
	}

	//this is used to remove bonuses if the boost should no longer be applied (e.g. the players weapon change so a perk does not apply)
	public void removeModifier(String sourceObjectName)
	{
		ModifierData modifierData = this.modifierData.get(sourceObjectName);
		if (modifierData != null)
		{
			for (Map.Entry<BonusTypes, Double> entry : modifierData.getModifiers().entrySet())
			{
				BonusTypes bonusTypes = entry.getKey();
				Double value = entry.getValue();
				switch (bonusTypes)
				{
					case DAMAGE_ADDITIVE -> miscModifiers.removeAdditiveWeaponDamageBonus(value);
					case DAMAGE_MULTIPLICATIVE -> miscModifiers.removeMultiplicativeWeaponDamageBonus(value);
					case PENETRATION_PHYSICAL -> miscModifiers.removePenetrationPhysicalBonus(value);
					case PENETRATION_ENERGY -> miscModifiers.removePenetrationEnergyBonus(value);
					case PENETRATION_RADIATION -> miscModifiers.removePenetrationRadiationBonus(value);
					case PENETRATION_POISON -> miscModifiers.removePenetrationPoisonBonus(value);
					case ATTACKSPEED -> miscModifiers.removeAttackSpeedBonus(value);
					case RELOADSPEED -> miscModifiers.removeReloadSpeedBonus(value);
					case ACCURACY -> miscModifiers.removeAccuracyBonus(value);
					case RANGE -> miscModifiers.removeRangeBonus(value);
					case SNEAK -> miscModifiers.removeSneakBonus(value);
					case CRITICAL -> miscModifiers.removeCriticalBonus(value);
					case SPECIAL_STRENGTH, SPECIAL_PERCEPTION, SPECIAL_ENDURANCE,
						SPECIAL_CHARISMA, SPECIAL_INTELLIGENCE, SPECIAL_AGILITY,
						SPECIAL_LUCK -> specialModifier.removeSpecialBonus(bonusTypes, value);
				}
			}
			this.modifierData.remove(sourceObjectName);
		}
	}

	//todo
	public void updateModifier()
	{

	}

	@PreDestroy
	public void clear()
	{
		if (this.modifierData != null) {
			this.modifierData.clear();
		}
		this.modifierData = null;
	}


	@EventListener
	public void onConditionChangedEvent(ConditionChangedEvent event)
	{
		removeModifier(event.getObjectName());
	}
}



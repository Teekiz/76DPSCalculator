package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdditionalContextService<T>
{
	/*
	 - This service is to provide additional context for modifiers that cannot be applied to simple data formats,
	 - or require additional logic checks. This includes mutations and perks such as adrenal reaction, herbivore/carnivore and suppressor.
	 - if a modifier has a mix of conditional and unconditional effects, set the conditional effect to more context required and then create additional context for that object.
	 */

	@Autowired
	private LoadoutManager loadoutManager;

	public Map.Entry<ModifierTypes, T> getAdditionalContext(String contextName)
	{
		switch (contextName)
		{
			case "ADRENALREACTION" : return (Map.Entry<ModifierTypes, T>) contextAdrenalReaction();
			case "BALLISTICBOCK" : return (Map.Entry<ModifierTypes, T>) contextBallisticBock();
			case "FURY" : return (Map.Entry<ModifierTypes, T>) contextFury();
			case "TESTCONDITION" : return (Map.Entry<ModifierTypes, T>) contextTestCondition();
		}
		return null;
	}

	//todo - handle using SPeL - !!!!
	private Map.Entry<ModifierTypes, Number> contextAdrenalReaction()
	{
		double hpPercentage = loadoutManager.getLoadout().getPlayerManager().getPlayer().getHealthPercentage();

		TreeMap<Double, Double> adrenalReactionMap = new TreeMap<>();
		adrenalReactionMap.put(0.0, 0.50);
		adrenalReactionMap.put(20.0, 0.50);
		adrenalReactionMap.put(30.0, 0.44);
		adrenalReactionMap.put(40.0, 0.38);
		adrenalReactionMap.put(50.0, 0.31);
		adrenalReactionMap.put(60.0, 0.25);
		adrenalReactionMap.put(70.0, 0.19);
		adrenalReactionMap.put(80.0, 0.13);
		adrenalReactionMap.put(90.0, 0.06);
		adrenalReactionMap.put(100.0, 0.0);

		Number value = adrenalReactionMap.floorEntry(hpPercentage).getValue();
		return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, value);
	}

	private Map.Entry<ModifierTypes, Number> contextBallisticBock()
	{
		if (loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getDamageType().equals(DamageType.BALLISTIC))
		{
			return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, 0.15);
		}
		return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, 0);
	}

	private Map.Entry<ModifierTypes, Number> contextFury()
	{
		if (loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getWeaponType().equals(WeaponType.ONEHANDED)
			|| loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getWeaponType().equals(WeaponType.TWOHANDED))
		{
			return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, 0.30);
		}
		return new AbstractMap.SimpleEntry<>(ModifierTypes.DAMAGE_ADDITIVE, 0);
	}

	private Map.Entry<ModifierTypes, Number> contextTestCondition()
	{
		if (loadoutManager.getLoadout().getWeaponManager().getCurrentWeapon().getDamageType().equals(DamageType.BALLISTIC))
		{
			return new AbstractMap.SimpleEntry<>(ModifierTypes.SPECIAL_CHARISMA, 5);
		}
		return new AbstractMap.SimpleEntry<>(ModifierTypes.SPECIAL_CHARISMA, 0);
	}
}

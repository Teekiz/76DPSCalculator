package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.util.AbstractMap;
import java.util.HashMap;
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
	 */

	@Autowired
	private LoadoutManager loadoutManager;

	public Map.Entry<ModifierTypes, T> getAdditionalContext(String contextName)
	{
		switch (contextName)
		{
			case "ADRENALREACTION" : return (Map.Entry<ModifierTypes, T>) contextAdrenalReaction();

		}
		return null;
	}

	//todo - handle using SPeL
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
}

package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

@Service
public class ConditionCalculationService
{
	//This service is used to write conditions programmatically that SPeL can call, useful for complex conditions that SPeL can't handle
	public Map.Entry<ModifierTypes, Number> getAdrenalReactionValue(double hpPercentage)
	{
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

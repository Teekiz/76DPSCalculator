package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import java.util.HashMap;

/**
 * An interface used to represent objects that can have a legendary effect.
 */
public interface LegendaryEffectCompatible
{
	void modifyLegendaryEffect(StarType starType, LegendaryEffect legendaryEffect);
	HashMap<StarType, LegendaryEffectSlot> getLegendaryEffects();
}

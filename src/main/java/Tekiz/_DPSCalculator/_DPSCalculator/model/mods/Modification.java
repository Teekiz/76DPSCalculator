package Tekiz._DPSCalculator._DPSCalculator.model.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModSubType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import java.io.Serializable;

public interface Modification extends Serializable, Modifier
{
	String id();
	String name();
	String alias();
	ModType modType();
	ModSubType modSubType();
}

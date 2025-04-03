package Tekiz._DPSCalculator._DPSCalculator.model.enums.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import lombok.Getter;

@Getter
public enum ModType
{
	//RANGED WEAPONS
	RECEIVER(Receiver.class),

	//ARMOUR
	MATERIAL(Material.class),
	MISCELLANEOUS(Miscellaneous.class);

	final Class<?> modClass;

	ModType(Class<?> modClass){
		this.modClass = modClass;
	}

}

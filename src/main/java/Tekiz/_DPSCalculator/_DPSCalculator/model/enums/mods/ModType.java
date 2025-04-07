package Tekiz._DPSCalculator._DPSCalculator.model.enums.mods;

import lombok.Getter;

@Getter
public enum ModType
{
	//RANGED WEAPONS
	RECEIVER("Receiver"),

	//ARMOUR
	MATERIAL("Material"),
	MISCELLANEOUS("Miscellaneous");

	final String name;
	ModType(String name){
		this.name = name;
	}

}

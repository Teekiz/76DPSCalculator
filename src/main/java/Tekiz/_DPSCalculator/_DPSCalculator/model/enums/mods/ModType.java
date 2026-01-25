package Tekiz._DPSCalculator._DPSCalculator.model.enums.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import lombok.Getter;

@Getter
public enum ModType
{
	//RANGED WEAPONS
	RECEIVER("Receiver", WeaponMod.class),
	BARREL("Barrel", WeaponMod.class),
	GRIP("Grip", WeaponMod.class),
	MAGAZINE("Magazine", WeaponMod.class),
	SIGHTS("Sights", WeaponMod.class),
	MUZZLE("Muzzle", WeaponMod.class),

	//ARMOUR
	MATERIAL("Material", ArmourMod.class),
	MISCELLANEOUS("Miscellaneous", ArmourMod.class);

	final String name;
	final Class<?> classType;
	ModType(String name, Class<?> classType){
		this.name = name;
		this.classType = classType;
	}

	/**
	 * A method to get the class type associated with the modification.
	 * @param modType The mod type to check against.
	 * @return The relevant classType (or null if not available)
	 */
	public static Class<?> getClassType(ModType modType){
		return modType.getClassType();
	}
}

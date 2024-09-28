package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/** Represents an armour piece that may be added to a user's loadout. */
@Getter
@AllArgsConstructor
public class Armour
{
	/** The name of the armour. The user will be able to see the given value. */
	private String armourName;

	/** The level of the armour that the user will need to meet before it can be worn. The resistances provided by the armour are affected by its level. */
	private int armourLevel;

	/** The type of the armour. Each armour type has specific limit to how many of each type can be worn. Armour or power armour consists of 6 pieces but only one type of
	 * under armour can be worn at any given time */
	private ArmourType armourType;

	/** The slot the armour piece takes. This is dependent on the armour type. */
	private ArmourPiece armourPiece;

	/** The set the armour is a part of. If a set provides a bonus, this will determine it. */
	private ArmourSet armourSet;

	/** A {@link HashMap} containing the level ({@link Integer}) and resistances ({@link ArmourResistance}) that the armour provides. */
	private HashMap<Integer, ArmourResistance> armourResistance;

	//mods
	/** The {@link Material} mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	private Material armourMaterial;
	/** The {@link Miscellaneous} mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	private Miscellaneous armourMisc;

	//todo - add check to ensure piece matches
	/**
	 * A method that is used to make modifications to the armour.
	 * @param armourMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ArmourMod} object.
	 */
	public void setMod(ArmourMod armourMod)
	{
		switch (armourMod)
		{
			case Material material -> armourMaterial = material;
			case Miscellaneous miscellaneous -> armourMisc = miscellaneous;
			default -> {}
		}
	}
}

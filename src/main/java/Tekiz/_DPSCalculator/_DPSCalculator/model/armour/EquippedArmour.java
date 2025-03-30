package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourSet;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class EquippedArmour
{
	private final Set<ArmourPiece> equippedArmourPieces = new HashSet<>();
	private final Set<PowerArmourPiece> equippedPowerArmourPieces = new HashSet<>();
	private UnderArmour equippedUnderArmour;

	private final Set<ArmourSet> armourSetBonuses = new HashSet<>();

	/**
	 * A method used to add or replace equipped armour in that slot.
	 * @param armour The armour to add or replace.
	 */
	public void addArmour(Armour armour)
	{
		removeArmourSetEffect(armour.armourSet);

		switch (armour)
		{
			case ArmourPiece armourPiece -> {
				equippedArmourPieces.removeIf(current -> current.armourPiece.equals(armour.armourPiece));
				equippedArmourPieces.add(armourPiece);
			}
			case PowerArmourPiece powerArmourPiece -> {
				equippedPowerArmourPieces.removeIf(current -> current.armourPiece.equals(armour.armourPiece));
				equippedPowerArmourPieces.add(powerArmourPiece);
			}
			case UnderArmour underArmour -> equippedUnderArmour = underArmour;
			default -> {}
		}
	}

	/**
	 * A method used to remove an existing set effect if no pieces of armour currently use it.
	 * @param armourSet The armour set of the new armour mod.
	 */
	private void removeArmourSetEffect(ArmourSet armourSet){
		boolean inArmourPieces= equippedArmourPieces.stream().anyMatch(exiting -> exiting.armourSet.equals(armourSet));
		boolean inPowerArmourPieces= equippedPowerArmourPieces.stream().anyMatch(exiting -> exiting.armourSet.equals(armourSet));
		boolean inUnderArmour = equippedUnderArmour != null && equippedUnderArmour.armourSet.equals(armourSet);

		if (!inArmourPieces && !inPowerArmourPieces && !inUnderArmour){
			armourSetBonuses.remove(armourSet);
		}
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EquippedArmour
{	@Setter
	private boolean isCurrentlyInPowerArmour;

	private final Set<OverArmourPiece> equippedOverArmourPieces = new HashSet<>();
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
			case OverArmourPiece overArmourPiece -> {
				equippedOverArmourPieces.removeIf(current -> current.armourPiece.equals(armour.armourPiece));
				equippedOverArmourPieces.add(overArmourPiece);
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
		boolean inArmourPieces= equippedOverArmourPieces.stream().anyMatch(exiting -> exiting.armourSet.equals(armourSet));
		boolean inPowerArmourPieces= equippedPowerArmourPieces.stream().anyMatch(exiting -> exiting.armourSet.equals(armourSet));
		boolean inUnderArmour = equippedUnderArmour != null && equippedUnderArmour.armourSet.equals(armourSet);

		if (!inArmourPieces && !inPowerArmourPieces && !inUnderArmour){
			armourSetBonuses.remove(armourSet);
		}
	}

	/**
	 * A method used to calculate the resistances based on the equipped armour.
	 * @return The current armour resistances.
	 */
	public ArmourResistance getCurrentResistanceBonus()
	{
		int[] tempValuesArray = new int[6];

		//If the player is currently in power armour, ignore the under armour and over (regular) amour pieces
		if (isCurrentlyInPowerArmour){
			equippedPowerArmourPieces.stream()
				.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
				.filter(Objects::nonNull)
				.forEach(resistance -> updateArrayList(resistance, tempValuesArray));

			//if the player does not have a helmet equipped, use the over armour (regular) helmet in its place.
			boolean hasPowerArmourHelmetEquipped = equippedPowerArmourPieces.stream()
				.anyMatch(piece -> piece.getArmourPiece().equals(ArmourPiece.HELMET));

			if (!hasPowerArmourHelmetEquipped) {
				equippedOverArmourPieces.stream()
					.filter(piece -> piece.getArmourPiece().equals(ArmourPiece.HELMET))
					.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
					.filter(Objects::nonNull)
					.findFirst()
					.ifPresent(helmetResistance -> updateArrayList(helmetResistance, tempValuesArray));
			}

			//applying the frame resistances
			tempValuesArray[0] += 60;
			tempValuesArray[1] += 60;
			tempValuesArray[2] += 60;

		} else {
			//if the player is wearing under armour, apply the under armour resistances.
			if (equippedUnderArmour != null){
				updateArrayList(equippedUnderArmour.getArmourResistance().get(equippedUnderArmour.getArmourLevel()), tempValuesArray);
			}

			equippedOverArmourPieces.stream()
				.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
				.filter(Objects::nonNull)
				.forEach(resistance -> updateArrayList(resistance, tempValuesArray));
		}

		return new ArmourResistance(tempValuesArray[0], tempValuesArray[1],
			tempValuesArray[2], tempValuesArray[3], tempValuesArray[4], tempValuesArray[5]);
	}

	/**
	 * A helper method to update the existing array.
	 * @param armourResistance The armour resistance with the new values to add.
	 * @param currentArray The array to be updated.
	 * @return The temporary array with updated values.
	 */
	private int[] updateArrayList(ArmourResistance armourResistance, int[] currentArray)
	{
		if (armourResistance != null){
			currentArray[0] += armourResistance.damageResistance();
			currentArray[1] += armourResistance.energyResistance();
			currentArray[2] += armourResistance.radiationResistance();
			currentArray[3] += armourResistance.cryoResistance();
			currentArray[4] += armourResistance.fireResistance();
			currentArray[5] += armourResistance.poisonResistance();
		}

		return currentArray;
	}
}

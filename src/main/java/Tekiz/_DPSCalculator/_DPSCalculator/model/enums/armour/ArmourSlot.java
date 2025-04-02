package Tekiz._DPSCalculator._DPSCalculator.model.enums.armour;

import lombok.Getter;

/**
 * The place that the armour piece is currently applied to.
 */

@Getter
public enum ArmourSlot
{
	HELMET(ArmourPiece.HELMET),
	TORSO(ArmourPiece.TORSO),
	LEFT_ARM(ArmourPiece.ARMS),
	RIGHT_ARM(ArmourPiece.ARMS),
	LEFT_LEG(ArmourPiece.LEGS),
	RIGHT_LEG(ArmourPiece.LEGS),
	OTHER(ArmourPiece.OTHER);

	/** The slot where the armour may be assigned to. */
	private final ArmourPiece armourPiece;
	ArmourSlot(ArmourPiece armourPiece){
		this.armourPiece = armourPiece;
	}
}

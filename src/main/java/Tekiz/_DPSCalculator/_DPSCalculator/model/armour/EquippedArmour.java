package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourSetEffects;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EquippedArmour
{	@Setter
	private boolean isCurrentlyInPowerArmour;

	private final Set<OverArmourPiece> equippedOverArmourPieces = new HashSet<>();
	private final Set<PowerArmourPiece> equippedPowerArmourPieces = new HashSet<>();
	private UnderArmour equippedUnderArmour;

	private final Set<ArmourSetEffects> armourSetEffectsBonuses = new HashSet<>();

	/**
	 * A method used to add or replace equipped armour in that slot.
	 * @param armour The armour to add or replace.
	 */
	public void addArmour(Armour armour)
	{
		removeArmour(armour.armourType, armour.armourSlot);

		switch (armour)
		{
			case OverArmourPiece overArmourPiece -> {
				equippedOverArmourPieces.add(overArmourPiece);
			}
			case PowerArmourPiece powerArmourPiece -> {
				equippedPowerArmourPieces.add(powerArmourPiece);
			}
			case UnderArmour underArmour -> equippedUnderArmour = underArmour;
			default -> {}
		}
	}

	/**
	 * A method used to add or replace equipped armour in that slot.
	 * @param armourType The type of armour to add or replace.
	 * @param armourSlot The place where the armour will be placed.
	 */
	public void removeArmour(ArmourType armourType, ArmourSlot armourSlot)
	{
		Armour currentlyEquippedArmour = null;

		switch (armourType) {
			case UNDER_ARMOUR -> {
				currentlyEquippedArmour = equippedUnderArmour;
				equippedUnderArmour = null;
			}
			case BODY_SUIT -> {
				// todo - add body suit code
			}
			case ARMOUR -> {
				currentlyEquippedArmour = (OverArmourPiece) getArmourInSlot(armourType, armourSlot);
				equippedOverArmourPieces.remove(currentlyEquippedArmour);
			}
			case POWER_ARMOUR -> {
				currentlyEquippedArmour = (PowerArmourPiece) getArmourInSlot(armourType, armourSlot);
				equippedPowerArmourPieces.remove(currentlyEquippedArmour);
			}
		}

		if (currentlyEquippedArmour != null) {
			removeArmourSetEffect(currentlyEquippedArmour.armourSet);
		}

	}

	/**
	 * Retrieves a piece of armour from the armour slot.
	 * @param armourType The type of armour to be retrieved.
	 * @param armourSlot The place where the armour is assigned.
	 * @return The {@link Armour} or {@code null} if a slot is not found.
	 */
	public Armour getArmourInSlot(ArmourType armourType, ArmourSlot armourSlot)
	{
		if (armourType == null || armourSlot == null){
			return null;
		}

		switch (armourType) {
			case UNDER_ARMOUR -> {
				return equippedUnderArmour;
			}
			case BODY_SUIT -> {
				// todo - add body suit code
			}
			case ARMOUR -> {
				return equippedOverArmourPieces.stream()
					.filter(Objects::nonNull)
					.filter(armour -> armour.getArmourSlot() != null && armour.getArmourSlot().equals(armourSlot))
					.findFirst()
					.orElse(null);
			}
			case POWER_ARMOUR -> {
				return equippedPowerArmourPieces.stream()
					.filter(Objects::nonNull)
					.filter(armour -> armour.getArmourSlot() != null && armour.getArmourSlot().equals(armourSlot))
					.findFirst()
					.orElse(null);
			}
			default -> {
				return null;
			}
		}
		return null;
	}

	/**
	 * A method used to remove an existing set effect if no pieces of armour currently use it.
	 * @param armourSetName The armour set of the new armour mod.
	 */
	private void removeArmourSetEffect(String armourSetName){
		ArmourSetEffects armourSetEffects = armourSetEffectsBonuses.stream().filter(effect -> effect.name().equalsIgnoreCase(armourSetName)).findFirst().orElse(null);

		if (armourSetEffects == null){
			return;
		}

		boolean inArmourPieces= equippedOverArmourPieces.stream().filter(Objects::nonNull).anyMatch(exiting -> exiting.armourSet != null && exiting.armourSet.equals(armourSetName));
		boolean inPowerArmourPieces= equippedPowerArmourPieces.stream().anyMatch(exiting -> exiting.armourSet.equals(armourSetName));
		boolean inUnderArmour = equippedUnderArmour != null && equippedUnderArmour.armourSet.equals(armourSetName);

		if (!inArmourPieces && !inPowerArmourPieces && !inUnderArmour){
			armourSetEffectsBonuses.remove(armourSetEffects);
		}
	}

	/**
	 * A method used to calculate the resistances based on the equipped armour.
	 * @return The current armour resistances.
	 */
	@JsonIgnore
	public ArmourResistance getCurrentResistanceBonus()
	{
		int[] tempValuesArray = new int[6];

		//If the player is currently in power armour, ignore the under armour and over (regular) amour pieces
		if (isCurrentlyInPowerArmour){
			equippedPowerArmourPieces.stream()
				.filter(Objects::nonNull)
				.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
				.forEach(resistance -> updateArrayList(resistance, tempValuesArray));

			//if the player does not have a helmet equipped, use the over armour (regular) helmet in its place.
			boolean hasPowerArmourHelmetEquipped = equippedPowerArmourPieces.stream()
				.anyMatch(piece -> piece.getArmourSlot().equals(ArmourSlot.HELMET));

			if (!hasPowerArmourHelmetEquipped) {
				equippedOverArmourPieces.stream()
					.filter(Objects::nonNull)
					.filter(piece -> piece.getArmourSlot().equals(ArmourSlot.HELMET))
					.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
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
				.filter(Objects::nonNull)
				.map(piece -> piece.getArmourResistance().get(piece.getArmourLevel()))
				.forEach(resistance -> updateArrayList(resistance, tempValuesArray));
		}

		return new ArmourResistance(tempValuesArray[0], tempValuesArray[1],
			tempValuesArray[2], tempValuesArray[3], tempValuesArray[4], tempValuesArray[5]);
	}

	/**
	 * A helper method to update the existing array.
	 *
	 * @param armourResistance The armour resistance with the new values to add.
	 * @param currentArray     The array to be updated.
	 */
	private void updateArrayList(ArmourResistance armourResistance, int[] currentArray)
	{
		if (armourResistance != null){
			currentArray[0] += armourResistance.damageResistance();
			currentArray[1] += armourResistance.energyResistance();
			currentArray[2] += armourResistance.radiationResistance();
			currentArray[3] += armourResistance.cryoResistance();
			currentArray[4] += armourResistance.fireResistance();
			currentArray[5] += armourResistance.poisonResistance();
		}

	}

	/**
	 * A method used to aggregate the legendary and modifier effects provided by the equipped armour.
	 * @return A list of provided effects.
	 */
	public List<LegendaryEffect> aggregateArmourEffects()
	{
		if (isCurrentlyInPowerArmour){
			return equippedPowerArmourPieces
				.stream()
				.flatMap(powerArmourPiece -> powerArmourPiece.getLegendaryEffects().getAllEffects().stream())
				.collect(Collectors.toList());
		} else {
			List<LegendaryEffect> modifiers = equippedUnderArmour != null ? equippedUnderArmour.getLegendaryEffects().getAllEffects() : new ArrayList<>();
			equippedOverArmourPieces
				.stream()
				.flatMap(overArmourPiece -> overArmourPiece.getLegendaryEffects().getAllEffects().stream())
				.forEach(modifiers::add);
			return modifiers;
		}
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class PerkRank implements Serializable
{
	private int currentRank;
	private int baseCost;
	private int maxRank;
	/**
	 * Sets the rank of the perk, ensuring that it remains within valid bounds.
	 * If the new rank is less than or equal to 0, the rank is set to 1.
	 * If the new rank exceeds the highest available rank in {@code effects}, the rank is capped.
	 *
	 * @param newRank The new rank to set for the perk.
	 */
	public void setCurrentRank(int newRank)
	{
		if (newRank <= 0) currentRank = 1;
		else currentRank = Math.min(newRank, maxRank);
	}
}

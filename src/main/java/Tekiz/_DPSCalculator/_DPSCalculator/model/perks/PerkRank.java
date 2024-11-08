package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An object that represents the rank and the restrictions placed on a rank.
 */
@Getter
@AllArgsConstructor
public class PerkRank implements Serializable
{
	@JsonProperty("currentRank")
	private int currentRank;
	@JsonProperty("baseCost")
	private int baseCost;
	@JsonProperty("maxRank")
	private int maxRank;
	/**
	 * Sets the rank of the perk, ensuring that it remains within valid bounds.
	 * If the new rank is less than or equal to 0, the rank is set to 1.
	 * If the new rank exceeds the highest available rank in {@code maxRank}, the rank is capped.
	 *
	 * @param newRank The new rank to set for the perk.
	 */
	@JsonIgnore
	public void setCurrentRank(int newRank)
	{
		if (newRank <= 0) currentRank = 1;
		else currentRank = Math.min(newRank, maxRank);
	}

	/**
	 * A method to calculate the points required to use this perk. Calculated by base cost + current rank - 1.
	 * @param temporaryRank An optional argument that can temporarily modify the perk cost (used for projected perk cost).
	 * @return A {@link Integer} of the points required to use this perk.
	 */
	@JsonIgnore
	public int getPointsCost(int... temporaryRank){
		//1 point is removed due to this point already being accounted for in the initial base cost.
		int rank = temporaryRank.length > 0 ? temporaryRank[0] : currentRank;
		return baseCost + rank - 1;
	}
}

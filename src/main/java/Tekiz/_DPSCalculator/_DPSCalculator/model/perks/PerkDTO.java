package Tekiz._DPSCalculator._DPSCalculator.model.perks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A data transfer object for an object representing perks.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerkDTO
{
	private int id;
	private String name;
	private String special;
	@Setter
	private int currentRank;
	private int baseCost;
	private int maxRank;
	private String description;
}

package Tekiz._DPSCalculator._DPSCalculator.model.calculations;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing {@link DPSDetails}.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DPSDetailsDTO
{
	private int loadoutID;
	private String weaponName;
	private List<ModifierDetails> modifierDetails;
	private double shotsPerSecond;
	private double timeToEmptyMagazine;
	private HashMap<String, Double> damagePerShot;
	private HashMap<String, Double> damagePerSecond;
	private double bodyPartMultiplier;
	private double damageResistMultiplier;
	private double totalDamagePerShot;
	private double totalDamagePerSecond;
}

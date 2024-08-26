package Tekiz._DPSCalculator._DPSCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public abstract class Weapon
{
	protected String weaponName;
	protected int[] weaponLevels;
	protected int[] weaponDamageValues;
	protected int apCost;
}

package Tekiz._DPSCalculator._DPSCalculator.model;

import java.util.Map;
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
	protected Map<Integer, Double> weaponDamageByLevel;
	protected int apCost;

	public abstract double getBaseDamage(int damage);
}

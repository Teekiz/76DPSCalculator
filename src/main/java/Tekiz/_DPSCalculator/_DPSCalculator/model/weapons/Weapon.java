package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
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
	protected WeaponType weaponType;
	protected DamageType damageType;
	protected Map<Integer, Double> weaponDamageByLevel;
	protected int apCost;

	//todo change this - receiver damage is additive
	public abstract double getBaseDamage(int damage);
}

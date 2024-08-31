package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter @Setter
@JsonDeserialize(builder = PistolBuilder.class)
public class Pistol extends Ranged
{
	private Receiver receiver;
	@Autowired
	public Pistol(String weaponName, WeaponType weaponType, HashMap<Integer, Double> weaponDamageByLevel, int apCost,
				  int magazineSize, int fireRate, int range, int accuracy, int projectileCount,
				  int criticalBonus, int rangedPenalty, double reloadTime, double attackSpeed,
				  double attackDelay, Receiver receiver)
	{
		super(weaponName,weaponType, weaponDamageByLevel, apCost,
			magazineSize, fireRate, range, accuracy, projectileCount,
			criticalBonus, rangedPenalty, reloadTime, attackSpeed,
			attackDelay);
		this.receiver = receiver;
	}
	public double getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}
	//Capacitor
	//Receiver
	//Dish
	//Barrel
	//Grip
	//Sights
	//Muzzle
}

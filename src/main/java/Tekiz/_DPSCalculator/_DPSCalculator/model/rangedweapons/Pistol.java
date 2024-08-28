package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Getter @Setter
@JsonDeserialize(builder = PistolBuilder.class)
public class Pistol extends Ranged
{
	private Receiver receiver;
	@Autowired
	public Pistol(String weaponName, HashMap<Integer, Double> weaponDamageByLevel, int apCost,
				  int magazineSize, int fireRate, int range, int accuracy, int projectileCount,
				  int criticalBonus, int rangedPenalty, double reloadTime, double attackSpeed,
				  double attackDelay, Receiver receiver)
	{
		super(weaponName,weaponDamageByLevel, apCost,
			magazineSize, fireRate, range, accuracy, projectileCount,
			criticalBonus, rangedPenalty, reloadTime, attackSpeed,
			attackDelay);
		this.receiver = receiver;
	}
	public double getBaseDamage(int weaponLevel)
	{
		double baseDamage = weaponDamageByLevel.get(weaponLevel);
		if (receiver != null)
		{
			baseDamage = baseDamage * receiver.getDamageChange();
		}
		return baseDamage;
	}
	//Capacitor
	//Receiver
	//Dish
	//Barrel
	//Grip
	//Sights
	//Muzzle
}

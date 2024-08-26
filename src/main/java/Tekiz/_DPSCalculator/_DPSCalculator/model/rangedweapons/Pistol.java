package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
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
	public Pistol(String weaponName, int[] weaponLevels, int[] weaponDamageValues, int apCost,
				  int magazineSize, int fireRate, int range, int accuracy, int projectileCount,
				  int criticalBonus, int rangedPenalty, double reloadTime, double attackSpeed,
				  double attackDelay, Receiver receiver)
	{
		super(weaponName, weaponLevels, weaponDamageValues, apCost,
			magazineSize, fireRate, range, accuracy, projectileCount,
			criticalBonus, rangedPenalty, reloadTime, attackSpeed,
			attackDelay);
		this.receiver = receiver;
	}

	//Capacitor
	//Receiver
	//Dish
	//Barrel
	//Grip
	//Sights
	//Muzzle
}

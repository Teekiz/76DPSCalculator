package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Getter @Setter
public class Pistol extends Ranged
{
	private Receiver receiver;
	@Autowired
	public Pistol(String weaponName, int[] weaponLevels, int[] weaponDamageValues, int apCost)
	{
		super(weaponName, weaponLevels, weaponDamageValues, apCost);
	}
	//Capacitor
	//Receiver
	//Dish
	//Barrel
	//Grip
	//Sights
	//Muzzle
}

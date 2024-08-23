package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.Receiver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Getter @Setter @RequiredArgsConstructor @Repository
public class Pistol extends Ranged
{
	private Receiver receiver;

	//todo autowired
	public Pistol(Receiver receiver)
	{
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

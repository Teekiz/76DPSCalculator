package Tekiz._DPSCalculator._DPSCalculator;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.Pistol;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver.Ammo;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver.Receivers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	static Pistol pistol = new Pistol();

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);

		pistol.setReceiver(new Receiver());
		pistol.getReceiver().setReceiver(Receivers.CALIBRATED);
		pistol.getReceiver().setAmmo(Ammo.FUSIONCELL);
	}

	@GetMapping
	public String getReceiver() {return pistol.getReceiver().getAmmoType().getAmmoName();}

	/*
	@GetMapping
	public String hello()
	{
		return "Hi";
	}

	 */

}

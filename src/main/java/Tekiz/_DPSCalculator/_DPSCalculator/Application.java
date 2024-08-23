package Tekiz._DPSCalculator._DPSCalculator;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.ModLoader;
import java.io.File;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	static ModLoader modLoader = new ModLoader();
	static File file = new File("src/main/resources/data/receivers.json");

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@GetMapping
	public String getReceiver() throws IOException
	{return modLoader.loadReceivers(file).get(0).getName();}

	/*
	@GetMapping
	public String hello()
	{
		return "Hi";
	}

	 */

}

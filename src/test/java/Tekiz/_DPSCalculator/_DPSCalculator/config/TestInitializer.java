package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class TestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext)
	{
		TestPropertyValues.of(
			"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
			"perk.data.file.path=src/test/resources/data/perkData/testPerks.json",
			"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json",
			"consumable.data.file.path=src/test/resources/data/consumableData/testConsumables.json",
			"mutation.data.file.path=src/test/resources/data/mutationData/testMutations.json"
		).applyTo(applicationContext.getEnvironment());
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.config.TestInitializer;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(initializers = TestInitializer.class)
public class PerkLoaderServiceTest
{
	@Autowired
	private PerkLoaderService perkLoaderService;

	@Test
	void testLoadPerk() throws IOException
	{
		String perkName = "HEAVYGUNNER";
		Perk perk = perkLoaderService.getPerk(perkName);
		assertNotNull(perk);
		assertEquals("Test Heavy Gunner", perk.getName());
	}
}

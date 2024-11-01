package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.PerkLoaderService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
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
		assertEquals("Test Heavy Gunner", perk.name());
	}

	@Test
	void testLoadPerks() throws IOException
	{
		List<Perk> perks = perkLoaderService.getAllPerks();
		Perk perk = perks.getFirst();
		assertNotNull(perk);
	}
}

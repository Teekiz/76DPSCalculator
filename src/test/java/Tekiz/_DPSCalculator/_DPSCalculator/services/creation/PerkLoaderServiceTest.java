package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Character.PerkTypes;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"perk.data.file.path=src/test/resources/data/perkData/testPerks.json"})
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
		assertEquals("Test Heavy Gunner", perk.getPerkName());
		assertEquals(PerkTypes.DAMAGE, perk.getPerkType());
	}
}

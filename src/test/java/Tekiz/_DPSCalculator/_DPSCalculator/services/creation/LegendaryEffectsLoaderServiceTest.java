package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LegendaryEffectsLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;

	@Test
	void testLoadLegendaryEffectsByName() throws IOException
	{
		String name = "ANTIARMOUR";
		LegendaryEffect legendaryEffect = dataLoaderService.loadDataByName(name, LegendaryEffect.class, null);
		assertNotNull(legendaryEffect);
		assertEquals("Anti-Armor", legendaryEffect.name());
		assertEquals(StarType._1STAR, legendaryEffect.starType());
	}

	@Test
	void testLoadLegendaryEffectsByID() throws IOException
	{
		String id = jsonIDMapper.getIDFromFileName("ANTIARMOUR");
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(id, LegendaryEffect.class, null);
		assertNotNull(legendaryEffect);
		assertEquals("Anti-Armor", legendaryEffect.name());
		assertEquals(StarType._1STAR, legendaryEffect.starType());
	}

	@Test
	void testLoadAllLegendaryEffects() throws IOException
	{
		List<LegendaryEffect> LegendaryEffects = dataLoaderService.loadAllData("LEGENDARYEFFECT", LegendaryEffect.class, null);
		assertNotNull(LegendaryEffects);
		assertEquals("Assassin's", LegendaryEffects.getFirst().name());
		assertEquals(StarType._1STAR, LegendaryEffects.get(1).starType());
	}
}

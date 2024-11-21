package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
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
public class ConsumableLoaderServiceTest
{
	@Autowired
	private DataLoaderService dataLoaderService;

	@Test
	void testLoadConsumable() throws IOException
	{
		String consumableName = "AGEDMIRELURKQUEENSTEAK";
		Consumable consumable = dataLoaderService.loadDataByName(consumableName, Consumable.class, null);
		assertNotNull(consumable);
		assertEquals("Aged mirelurk queen steak", consumable.name());
	}

	@Test
	void testLoadAllConsumables() throws IOException
	{
		List<Consumable> consumables = dataLoaderService.loadAllData("consumable", Consumable.class, null);
		assertNotNull(consumables);
		assertNotNull(consumables.stream()
			.filter(consumable -> consumable.name().equalsIgnoreCase("Ballistic Bock"))
			.findFirst()
			.orElse(null));
		assertNotNull(consumables.stream()
			.filter(consumable -> consumable.name().equalsIgnoreCase("Berry Mentats"))
			.findFirst()
			.orElse(null));
	}
}


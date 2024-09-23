package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
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
	private ConsumableLoaderService consumableLoaderService;

	@Test
	void testLoadConsumable() throws IOException
	{
		String consumableName = "AGEDMIRELURKQUEENSTEAK";
		Consumable consumable = consumableLoaderService.getConsumable(consumableName);
		assertNotNull(consumable);
		assertEquals("Test Aged mirelurk queen steak", consumable.getName());
	}

	@Test
	void testLoadAllConsumables() throws IOException
	{
		List<Consumable> consumables = consumableLoaderService.getAllConsumables();
		assertNotNull(consumables);
		assertEquals("Test Ballistic Bock", consumables.get(1).getName());
		assertEquals("Test Berry Mentats", consumables.get(2).getName());
	}
}


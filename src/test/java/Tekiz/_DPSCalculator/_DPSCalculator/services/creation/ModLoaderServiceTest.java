package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;

	@Test
	void testLoadReceiver() throws IOException
	{
		String receiverName = "AUTOMATIC";
		Receiver receiver = dataLoaderService.loadDataByName(receiverName, Receiver.class, null);
		assertNotNull(receiver);
		assertEquals("TestAutomatic", receiver.name());
	}

	@Test
	void testLoadAllReceivers() throws IOException
	{
		List<Receiver> receivers = dataLoaderService.loadAllData("modRe", Receiver.class, null);
		assertNotNull(receivers);
		assertEquals("TestAutomatic", receivers.get(0).name());
		assertFalse(receivers.get(0).isPrime());
		assertEquals("TestCalibrated", receivers.get(2).name());
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ModLoaderService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ModLoaderServiceTest
{
	@Autowired
	ModLoaderService modLoaderService;

	@Test
	void testLoadReceiver() throws IOException
	{
		String receiverName = "AUTOMATIC";
		Receiver receiver = modLoaderService.getReceiver(receiverName);
		assertNotNull(receiver);
		assertEquals("TestAutomatic", receiver.name());
	}

	@Test
	void testLoadAllReceivers() throws IOException
	{
		List<Receiver> receivers = modLoaderService.getAllReceivers();
		assertNotNull(receivers);
		assertEquals("TestAutomatic", receivers.get(0).name());
		assertFalse(receivers.get(0).isPrime());
		assertEquals("TestCalibrated", receivers.get(1).name());
	}
}

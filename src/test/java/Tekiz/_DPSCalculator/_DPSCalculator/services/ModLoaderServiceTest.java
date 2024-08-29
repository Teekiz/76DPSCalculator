package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json"})
public class ModLoaderServiceTest
{
	@Autowired
	private ModLoaderService modLoaderService;

	@Test
	void testLoadReceiver() throws IOException
	{
		String receiverName = "AUTOMATIC";
		Receiver receiver = modLoaderService.getReceiver(receiverName);
		assertNotNull(receiver);
		assertEquals("TestAutomatic", receiver.getName());
	}

	@Test
	void testLoadAllReceivers() throws IOException
	{
		List<Receiver> receivers = modLoaderService.getAllReceivers();
		assertNotNull(receivers);
		assertEquals("TestAutomatic", receivers.get(0).getName());
		assertFalse(receivers.get(0).isPrime());
		assertEquals("TestCalibrated", receivers.get(1).getName());
	}
}

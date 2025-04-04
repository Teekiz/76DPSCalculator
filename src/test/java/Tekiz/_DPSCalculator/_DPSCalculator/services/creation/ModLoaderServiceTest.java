package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
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
		WeaponMod weaponMod = dataLoaderService.loadDataByName(receiverName, WeaponMod.class, null);
		assertNotNull(weaponMod);
		assertEquals("TestAutomatic", weaponMod.name());
	}

	@Test
	void testLoadAllReceivers() throws IOException
	{
		List<WeaponMod> weaponMods = dataLoaderService.loadAllData("weaponMods", WeaponMod.class, null);
		assertNotNull(weaponMods);
		assertEquals("Default", weaponMods.get(0).name());
		assertEquals("TestCalibrated", weaponMods.get(2).name());
	}
}

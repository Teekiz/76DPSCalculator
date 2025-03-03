package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArmourLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;

	@Test
	void testLoadArmour() throws IOException
	{
		String armourName = "WOODCHEST";
		Armour armour = dataLoaderService.loadDataByName(armourName, Armour.class, null);
		assertNotNull(armour);
		assertEquals("Wood chest", armour.getName());
		assertEquals(37, armour.getArmourResistance().get(35).getDamageResistance());
	}

	@Test
	void testLoadAllArmour() throws IOException
	{
		List<Armour> armours = dataLoaderService.loadAllData("armour", Armour.class, null);
		assertNotNull(armours);
		assertEquals(ArmourType.ARMOUR, armours.getFirst().getArmourType());
		assertEquals(ArmourSet.WOOD, armours.getFirst().getArmourSet());
	}
}

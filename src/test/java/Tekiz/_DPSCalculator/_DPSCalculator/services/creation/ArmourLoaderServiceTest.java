package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Armour.ArmourSet;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Armour.ArmourType;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"armour.data.file.path=src/test/resources/data/armourData/testArmour.json"})
public class ArmourLoaderServiceTest
{
	@Autowired
	private ArmourLoaderService armourLoaderService;

	@Test
	void testLoadArmour() throws IOException
	{
		String armourName = "WOODCHEST";
		Armour armour = armourLoaderService.getArmour(armourName);
		assertNotNull(armour);
		assertEquals("Wood chest", armour.getArmourName());
		assertEquals(37, armour.getArmourResistance().get(35).getDamageResistance());
	}

	@Test
	void testLoadAllArmour() throws IOException
	{
		List<Armour> armours = armourLoaderService.getAllArmourPieces();
		assertNotNull(armours);
		assertEquals(ArmourType.ARMOUR, armours.get(0).getArmourType());
		assertEquals(ArmourSet.WOOD, armours.get(0).getArmourSet());
	}
}

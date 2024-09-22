package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.config.TestInitializer;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(initializers = TestInitializer.class)
public class ArmourModLoaderServiceTest
{
	@Autowired
	private ArmourLoaderService armourLoaderService;
	@Autowired
	private ArmourModLoaderService armourModLoaderService;

	@Test
	public void testMaterialLoader() throws IOException
	{
		String materialMod = "BOILEDLEATHERCHEST";
		Material material = armourModLoaderService.getMaterial(materialMod);
		assertNotNull(material);
		assertEquals("Boiled leather", material.getModName());

		String armourPiece = "WOODCHEST";
		Armour armour = armourLoaderService.getArmour(armourPiece);
		assertNotNull(armour);
		armour.setMod(material);
		assertNotNull(armour.getArmourMaterial());
		assertEquals(ArmourPiece.TORSO, armour.getArmourMaterial().getArmourPiece());
	}
}

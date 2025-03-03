package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArmourModLoaderServiceTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;

	@Test
	public void testMaterialLoader() throws IOException
	{
		String materialMod = "BOILEDLEATHERCHEST";
		Material material = dataLoaderService.loadDataByName(materialMod, Material.class, null);
		assertNotNull(material);
		assertEquals("Boiled leather", material.getName());

		String armourPiece = "WOODCHEST";
		Armour armour = dataLoaderService.loadDataByName(armourPiece, Armour.class, null);
		assertNotNull(armour);
		armour.setMod(material);
		assertNotNull(armour.getArmourMaterial());
		assertEquals(ArmourPiece.TORSO, armour.getArmourMaterial().getArmourPiece());
	}
}

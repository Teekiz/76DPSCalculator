package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.OverArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.ArmourFactory;
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
	@Autowired
	ArmourFactory armourFactory;

	@Test
	public void testMaterialLoader() throws IOException
	{
		String materialMod = "BOILEDLEATHERCHEST";
		Material material = dataLoaderService.loadDataByName(materialMod, Material.class, null);
		assertNotNull(material);
		assertEquals("Boiled leather", material.getName());

		String armourPiece = "WOODCHEST";
		Armour armour = dataLoaderService.loadDataByName(armourPiece, Armour.class, armourFactory);
		assertNotNull(armour);
		armour.setMod(material);
		assertEquals(OverArmourPiece.class, armour.getClass());

		OverArmourPiece overArmourPiece = (OverArmourPiece) armour;
		assertNotNull(overArmourPiece.getArmourMaterial());
		assertEquals(ArmourPiece.TORSO, overArmourPiece.getArmourMaterial().getArmourPiece());
	}
}

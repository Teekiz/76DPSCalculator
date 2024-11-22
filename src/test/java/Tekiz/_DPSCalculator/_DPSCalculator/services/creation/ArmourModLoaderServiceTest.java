package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ArmourModLoaderServiceTest
{
	@Autowired
	private DataLoaderService dataLoaderService;

	@Test
	public void testMaterialLoader() throws IOException
	{
		String materialMod = "BOILEDLEATHERCHEST";
		Material material = dataLoaderService.loadDataByName(materialMod, Material.class, null);
		assertNotNull(material);
		assertEquals("Boiled leather", material.getModName());

		String armourPiece = "WOODCHEST";
		Armour armour = dataLoaderService.loadDataByName(armourPiece, Armour.class, null);
		assertNotNull(armour);
		armour.setMod(material);
		assertNotNull(armour.getArmourMaterial());
		assertEquals(ArmourPiece.TORSO, armour.getArmourMaterial().getArmourPiece());
	}
}

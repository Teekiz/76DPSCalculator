package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.Weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.RangedWeaponBuilder;
import Tekiz._DPSCalculator._DPSCalculator.services.factory.WeaponFactory;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"weapon.data.file.path=src/test/resources/data/weaponData/testWeapons.json",
	"receiver.data.file.path=src/test/resources/data/weaponData/testReceivers.json"})
public class WeaponBuilderTest
{
	@Autowired
	private WeaponFactory weaponFactory;

	@Test
	void testPistolBuilderWithOptionalData() throws IOException
	{
		HashMap<Integer, Double> weaponDamageMap = new HashMap<>();
		weaponDamageMap.put(5, 15.0);
		weaponDamageMap.put(15, 18.0);
		weaponDamageMap.put(25, 21.0);
		weaponDamageMap.put(35, 24.0);
		weaponDamageMap.put(45, 28.0);

		RangedWeapon pistol = new RangedWeaponBuilder("10mm Pistol", WeaponType.PISTOL, DamageType.BALLISTIC, weaponDamageMap, 20).setAccuracy(20).build();
		assertNotNull(pistol);
		assertEquals("10mm Pistol", pistol.getWeaponName());
		assertEquals(21, pistol.getWeaponDamageByLevel().get(25));
		assertEquals(20, pistol.getApCost());
	}
}

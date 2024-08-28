package Tekiz._DPSCalculator._DPSCalculator.services;

import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.Pistol;
import Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.PistolBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class WeaponBuilderTest
{
	@Mock
	private ObjectMapper objectMapper;
	@InjectMocks
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

		Pistol pistol = new PistolBuilder("10mm Pistol", weaponDamageMap, 20).setAccuracy(20).build();
		assertNotNull(pistol);
		assertEquals("10mm Pistol", pistol.getWeaponName());
		assertEquals(21, pistol.getWeaponDamageByLevel().get(25));
		assertEquals(20, pistol.getApCost());
	}
}

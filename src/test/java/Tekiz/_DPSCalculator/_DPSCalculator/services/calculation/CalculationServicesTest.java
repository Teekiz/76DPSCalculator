package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.ModType;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@ActiveProfiles("test")
public class CalculationServicesTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	DamageCalculationService calculator;

	@Test
	public void testWithBaseDamage() throws IOException
	{
		loadoutManager.deleteAllLoadouts();

		//setting the conditions for the two base weapons
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);

		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENTTWO");

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		assertEquals(39.2, calculator.calculateOutgoingDamage());

		//removing the perk should reduce the damage by 20%
		loadoutManager.getLoadout().getPerkManager().removePerk("TestEventPerk");
		assertEquals(33.6, calculator.calculateOutgoingDamage());

		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testMultiplicativeDamage() throws IOException
	{
		loadoutManager.deleteAllLoadouts();

		//weapon damage at level 45 is 28.0, each perk and consumable adds 0.2 extra damage and the receiver doesn't modify the damage
		//28.0 * (1 + 0.2 + 0.2 + 0) = 39.2
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");
		loadoutManager.getLoadout().getWeaponManager().modifyWeapon("CALIBRATE", ModType.RECEIVER);
		loadoutManager.getLoadout().getPerkManager().addPerk("TESTEVENT");
		loadoutManager.getLoadout().getConsumableManager().addConsumable("TESTEVENTTWO");
		assertEquals(39.2, calculator.calculateOutgoingDamage());

		//level 1 tenderizer should add 5% extra damage on top of the existing damage
		//39.2 * (1 + 0.05) = 41.16 (41.2)
		loadoutManager.getLoadout().getPerkManager().addPerk("TENDERIZER");
		assertEquals(41.2, calculator.calculateOutgoingDamage());

	}
}

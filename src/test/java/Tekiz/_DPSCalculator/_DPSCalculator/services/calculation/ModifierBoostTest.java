package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class ModifierBoostTest
{
	@Autowired
	LoadoutManager loadoutManager;

	@Autowired
	DamageCalculationService damageCalculationService;

	@Test
	public void checkSourceTypes() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout();
		loadout.getConsumableManager().addConsumable("FURY");
		Consumable consumable = loadout.getConsumableManager().getConsumables().keySet().stream()
			.filter(consumableObject -> consumableObject.getName().equalsIgnoreCase("Fury"))
			.findFirst()
			.orElse(null);


		assertNotNull(consumable);
		assertEquals(ModifierSource.CONSUMABLE_CHEMS, consumable.getModifierSource());

		loadoutManager.getLoadout().getMutationManager().addMutation("ADRENALREACTION");
		Mutation mutation = loadoutManager.getLoadout().getMutationManager().getMutations().stream().findFirst().orElse(null);
		assertNotNull(mutation);
		assertEquals(ModifierSource.MUTATION_POSITIVE, mutation.getPositiveEffects().getModifierSource());
		assertEquals(ModifierSource.MUTATION_NEGATIVE, mutation.getNegativeEffects().getModifierSource());
		loadoutManager.deleteAllLoadouts();
	}

	@Test
	public void testModifierBoostMutations() throws IOException
	{
		loadoutManager.getLoadout().getMutationManager().addMutation("ADRENALREACTION");
		loadoutManager.getLoadout().getPlayerManager().getPlayer().setCurrentHP(98.0);
		loadoutManager.getLoadout().getWeaponManager().setWeapon("10MMPISTOL");

		//hp is set to 98.0 (39.2%), so it sound return 0.38 additional damage
		//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 35.8
		//28 * (1 + 0.38 - 0.1) = 35.84 (35.8)

		assertEquals(35.8, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.getLoadout().getPerkManager().addPerk("STRANGEINNUMBERS");
		//hp is still set to 39.2% or 0.38 additive damage
		//0.38 * 1.25 = 0.475 (0.48 rounded up)
		//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 38.64
		//28 * (1 + 0.48 - 0.1) = 38.64 (38.6)
		assertEquals(38.6, damageCalculationService.calculateOutgoingDamage(loadoutManager.getLoadout()));

		loadoutManager.deleteAllLoadouts();
	}
}

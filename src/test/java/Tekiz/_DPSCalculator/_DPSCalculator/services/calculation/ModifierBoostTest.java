package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
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

	@Test
	public void checkSourceTypes() throws IOException
	{
		loadoutManager.getLoadout().getConsumableManager().addConsumable("FURY");
		Consumable consumable = loadoutManager.getLoadout().getConsumableManager().getConsumables().keySet().stream().findFirst().orElse(null);
		assertNotNull(consumable);
		assertEquals(ModifierSource.CONSUMABLE_CHEMS, consumable.getModifierSource());

		loadoutManager.getLoadout().getMutationManager().addMutation("ADRENALREACTION");
		Mutation mutation = loadoutManager.getLoadout().getMutationManager().getMutations().stream().findFirst().orElse(null);
		assertNotNull(mutation);
		assertEquals(ModifierSource.MUTATION_POSITIVE, mutation.getPositiveEffects().getModifierSource());
		assertEquals(ModifierSource.MUTATION_NEGATIVE, mutation.getNegativeEffects().getModifierSource());
	}
}

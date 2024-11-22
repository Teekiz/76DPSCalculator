package Tekiz._DPSCalculator._DPSCalculator.services.calculation;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierSource;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ModifierBoostTest
{
	@Autowired
	UserLoadoutTracker userLoadoutTracker;
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PlayerManager playerManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	MutationManager mutationManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	DamageCalculationService damageCalculationService;

	@Test
	public void checkSourceTypes() throws IOException
	{
		log.debug("{}Running test - checkSourceTypes in ModifierBoostTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		consumableManager.addConsumable("consu3", loadout);//FURY
		Consumable consumable = loadout.getConsumables().keySet().stream()
			.filter(consumableObject -> consumableObject.name().equalsIgnoreCase("Fury"))//FURY
			.findFirst()
			.orElse(null);

		assertNotNull(consumable);
		assertEquals(ModifierSource.CONSUMABLE_CHEMS, consumable.modifierSource());

		mutationManager.addMutation("mutat1", loadout);//ADRENALREACTION
		Mutation mutation = loadout.getMutations().stream().findFirst().orElse(null);
		assertNotNull(mutation);
		assertEquals(ModifierSource.MUTATION_POSITIVE, mutation.positiveEffects().modifierSource());
		assertEquals(ModifierSource.MUTATION_NEGATIVE, mutation.negativeEffects().modifierSource());
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testModifierBoostMutations() throws IOException
	{
		log.debug("{}Running test - testModifierBoostMutations in ModifierBoostTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		mutationManager.addMutation("mutat1", loadout);//ADRENALREACTION
		playerManager.getPlayer(loadout).setCurrentHP(98.0);
		weaponManager.setWeapon("weapo2", loadout);//10MMPISTOL

		//hp is set to 98.0 (49.0%) max health is 200 (250 - 50), so it sound return 0.31 additional damage
		//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 33.9
		//28 * (1 + 0.31 - 0.1) = 33.9 (33.88)
		assertEquals(33.9, damageCalculationService.calculateOutgoingDamage(loadout));

		perkManager.addPerk("perks2", loadout);//STRANGEINNUMBERS
		//hp is set to 98.0 (49.0%) max health is 200 (250 - 50), so it sound return 0.31 additional damage
		//0.31 * 1.25 = 0.3875 (0.39 rounded up)
		//level 45 pistol damage is 28, with an automatic receiver reducing the damage down to 36.1
		//28 * (1 + 0.39 - 0.1) = 36.1 (36.12)
		assertEquals(36.1, damageCalculationService.calculateOutgoingDamage(loadout));

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

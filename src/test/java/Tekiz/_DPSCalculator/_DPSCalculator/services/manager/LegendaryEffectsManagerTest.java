package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectsMap;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.util.AssertionErrors.assertTrue;

public class LegendaryEffectsManagerTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;
	@Autowired
	LegendaryEffectManager legendaryEffectManager;
	@Autowired
	WeaponManager weaponManager;

	String ANTIARMOUR;
	String ASSASSINS;
	String _10MMPISTOL;

	@BeforeEach
	public void initializeVariables()
	{
		ANTIARMOUR = jsonIDMapper.getIDFromFileName("ANTIARMOUR");
		ASSASSINS = jsonIDMapper.getIDFromFileName("ASSASSINS");
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
	}

	@Test
	public void addingLegendaryEffect_ToWeapon() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		legendaryEffectManager.addLegendaryEffect(ANTIARMOUR, loadout.getWeapon(), loadout);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 1);
	}

	@Test
	public void addingLegendaryEffect_ToWeapon_WithImmutableSlot() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffect legendaryEffect = dataLoaderService.loadData(ANTIARMOUR, LegendaryEffect.class, null);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(legendaryEffect, false);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		legendaryEffectManager.addLegendaryEffect(ASSASSINS, loadout.getWeapon(), loadout);

		assertTrue("Loadout weapon effect has not been changed.",
			loadout.getWeapon().getLegendaryEffects().containsKey(legendaryEffect));
	}

	@Test
	public void removingLegendaryEffect_FromWeapon_WithImmutableSlot() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffect legendaryEffect = dataLoaderService.loadData(ANTIARMOUR, LegendaryEffect.class, null);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(legendaryEffect, false);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		legendaryEffectManager.removeLegendaryEffect(StarType._1STAR, loadout.getWeapon(), loadout);

		assertTrue("Loadout weapon effect has not been changed.",
			loadout.getWeapon().getLegendaryEffects().containsKey(legendaryEffect));
	}

	@Test
	public void removingLegendaryEffect_FromWeapon() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);
		legendaryEffectManager.addLegendaryEffect(ANTIARMOUR, loadout.getWeapon(), loadout);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 1);

		legendaryEffectManager.removeLegendaryEffect(StarType._1STAR, loadout.getWeapon(), loadout);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().isEmpty());
	}

	@Test
	public void addingLegendaryEffect_ToWeapon_WithNullSlot() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(null, true);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		//this shouldn't happen under normal circumstances.
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(ANTIARMOUR, LegendaryEffect.class, null);
		legendaryEffectsMap.addLegendaryEffect(legendaryEffect);

		assertTrue("Loadout weapon has 2 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 2);
	}

	@Test
	public void addingLegendaryEffect_ToWeapon_WithNullSlot_ImmutableSlot() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(null, false);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		//this shouldn't happen under normal circumstances.
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(ANTIARMOUR, LegendaryEffect.class, null);
		legendaryEffectsMap.addLegendaryEffect(legendaryEffect);

		assertTrue("Loadout weapon has 2 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 2);
	}

	@Test
	public void removingLegendaryEffect_FromWeapon_ObjectDoNotMatch() throws IOException
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(null, true);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		//this shouldn't happen under normal circumstances.
		LegendaryEffect legendaryEffect = dataLoaderService.loadData(ANTIARMOUR, LegendaryEffect.class, null);
		legendaryEffectsMap.remove(legendaryEffect);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 1);
	}

	@Test
	public void removingLegendaryEffect_FromWeapon_WithNullSlot()
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(null, true);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		//under normal circumstances, this shouldn't happen as interactions are handled by the manager
		legendaryEffectsMap.removeLegendaryEffect(null);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 1);
	}

	@Test
	public void removingLegendaryEffect_FromWeapon_WithNullSlot_ImmutableSlot()
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		LegendaryEffectsMap legendaryEffectsMap = new LegendaryEffectsMap();
		legendaryEffectsMap.put(null, false);
		RangedWeapon rangedWeapon = RangedWeapon.builder().legendaryEffects(legendaryEffectsMap).build();
		loadout.setWeapon(rangedWeapon);

		//under normal circumstances, this shouldn't happen as interactions are handled by the manager
		legendaryEffectsMap.removeLegendaryEffect(null);

		assertTrue("Loadout weapon has 1 legendary effect.",
			loadout.getWeapon().getLegendaryEffects().size() == 1);
	}

	@Test
	public void getAvailableLegendaryEffects() throws IOException
	{
		List<LegendaryEffect> effects = legendaryEffectManager.getAvailableLegendaryEffects(null, null);

		boolean containsMultipleStarTypes = effects.stream().map(LegendaryEffect::starType).distinct().count() > 1;
		boolean containsMultipleCategories = effects.stream().map(LegendaryEffect::categories).distinct().count() > 1;

		assertTrue("Contains multiple star types", containsMultipleStarTypes);
		assertTrue("Contains multiple categories", containsMultipleCategories);
	}

	@Test
	public void getAvailableLegendaryEffects_WithStarType() throws IOException
	{
		List<LegendaryEffect> effects = legendaryEffectManager.getAvailableLegendaryEffects(StarType._1STAR, null);

		boolean containsMultipleStarTypes = effects.stream().map(LegendaryEffect::starType).distinct().count() == 1;
		boolean containsMultipleCategories = effects.stream().map(LegendaryEffect::categories).distinct().count() > 1;

		assertTrue("Contains one star type", containsMultipleStarTypes);
		assertTrue("Contains multiple categories", containsMultipleCategories);
	}

	@Test
	public void getAvailableLegendaryEffects_WithCategory() throws IOException
	{
		List<LegendaryEffect> effects = legendaryEffectManager.getAvailableLegendaryEffects(null, Category.MELEE_WEAPONS);

		boolean containsMultipleStarTypes = effects.stream().map(LegendaryEffect::starType).distinct().count() > 1;
		boolean containsMultipleCategories = effects.stream().map(LegendaryEffect::categories).distinct().count() == 1;

		assertTrue("Contains multiple star type", containsMultipleStarTypes);
		assertTrue("Contains one category", containsMultipleCategories);
	}

	@Test
	public void getAvailableLegendaryEffects_WithStarType_WithCategory() throws IOException
	{
		List<LegendaryEffect> effects = legendaryEffectManager.getAvailableLegendaryEffects(StarType._1STAR, Category.ARMOUR);

		boolean containsMultipleStarTypes = effects.stream().map(LegendaryEffect::starType).distinct().count() == 1;
		boolean containsMultipleCategories = effects.stream().map(LegendaryEffect::categories).distinct().count() == 1;

		assertTrue("Contains one star type", containsMultipleStarTypes);
		assertTrue("Contains one category", containsMultipleCategories);
	}
}

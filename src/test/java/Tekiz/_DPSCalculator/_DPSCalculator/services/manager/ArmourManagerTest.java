package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.OverArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class ArmourManagerTest extends BaseTestClass
{
	@Autowired
	LoadoutManager loadoutManager;
	@Autowired
	ArmourManager armourManager;
	@Autowired
	PlayerManager playerManager;
	@Autowired
	LegendaryEffectManager legendaryEffectManager;

	String WOODCHEST;
	String WOODLEG;
	String BOILEDLEATHERCHEST;
	String CUSHIONED;
	String UNYIELDING;

	@BeforeEach
	public void initializeVariables()
	{
		WOODCHEST = jsonIDMapper.getIDFromFileName("WOODCHEST");
		WOODLEG = jsonIDMapper.getIDFromFileName("WOODLEG");
		BOILEDLEATHERCHEST = jsonIDMapper.getIDFromFileName("BOILEDLEATHERCHEST");
		CUSHIONED = jsonIDMapper.getIDFromFileName("CUSHIONED");
		UNYIELDING = jsonIDMapper.getIDFromFileName("UNYIELDING");
	}

	@Test
	public void addingArmour() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);

		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		armourManager.addArmour(WOODLEG, ArmourSlot.LEFT_LEG, loadout);

		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.LEFT_LEG));

		armourManager.addArmour(WOODLEG, ArmourSlot.RIGHT_LEG, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.LEFT_LEG));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.RIGHT_LEG));
	}

	@Test
	public void addingArmour_InInvalidSlot() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.LEFT_ARM, loadout);

		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.LEFT_ARM));
	}

	@Test
	public void addingArmour_InvalidID() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		assertThrows(ResourceNotFoundException.class, () -> armourManager.addArmour("InvalidIDISNOTAREALID", ArmourSlot.LEFT_ARM, loadout));
		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.LEFT_ARM));
	}

	@Test
	public void removingArmour() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);

		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		armourManager.addArmour(WOODLEG, ArmourSlot.LEFT_LEG, loadout);

		armourManager.removeArmour(ArmourType.ARMOUR, ArmourSlot.TORSO, loadout);
		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.LEFT_LEG));
	}

	@Test
	public void removingArmour_SlotDoesNotHaveItemEquipped()
	{
		Loadout loadout = loadoutManager.getLoadout(1);

		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		armourManager.removeArmour(ArmourType.ARMOUR, ArmourSlot.TORSO, loadout);
		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
	}

	@Test
	public void modifyingArmour_WithValidArmour() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		armourManager.modifyArmour(CUSHIONED, ArmourType.ARMOUR, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		OverArmourPiece piece = (OverArmourPiece)loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO);
		assertEquals("Cushioned", piece.getModifications().get(ModType.MISCELLANEOUS).getCurrentModification().name());
	}

	@Test
	public void modifyingArmour_WithInValidArmour() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		assertThrows(ResourceNotFoundException.class, () -> armourManager.modifyArmour(CUSHIONED, ArmourType.ARMOUR, ArmourSlot.TORSO, loadout));
		assertNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
	}

	@Test
	public void modifyingArmour_WithValidArmour_InvalidMod() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		assertThrows(ResourceNotFoundException.class, () -> armourManager.modifyArmour("NOTAREALMOD", ArmourType.ARMOUR, ArmourSlot.TORSO, loadout));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		OverArmourPiece piece = (OverArmourPiece)loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO);
		assertNull(piece.getModifications().get(ModType.MISCELLANEOUS).getCurrentModification());

		assertThrows(ResourceNotFoundException.class, () -> armourManager.modifyArmour(CUSHIONED, ArmourType.ARMOUR, ArmourSlot.LEFT_LEG, loadout));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		assertNull(piece.getModifications().get(ModType.MISCELLANEOUS).getCurrentModification());
	}

	@Test
	public void addingLegendaryEffect_WithValidArmour() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		legendaryEffectManager.addLegendaryEffect(UNYIELDING, loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO), loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		OverArmourPiece piece = (OverArmourPiece)loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO);
		assertEquals(1, piece.getLegendaryEffects().size());
		assertEquals("Unyielding", piece.getLegendaryEffects().getAllEffects().getFirst().name());
	}

	@Test
	public void addingLegendaryEffect_WithValidArmour_InvalidEffect() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		assertThrows(ResourceNotFoundException.class, () -> legendaryEffectManager.addLegendaryEffect("NOTAREALARMOURMOD", loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO), loadout));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		OverArmourPiece piece = (OverArmourPiece)loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO);
		assertEquals(0, piece.getLegendaryEffects().size());
	}

	@Test
	public void addingLegendaryEffect_WithValidArmour_IncorrectSlot() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		assertThrows(ResourceNotFoundException.class, () -> legendaryEffectManager.addLegendaryEffect(UNYIELDING, loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.OTHER), loadout));
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));
		OverArmourPiece piece = (OverArmourPiece) loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO);
		assertEquals(0, piece.getLegendaryEffects().size());
	}

	@Test
	public void addingLegendaryEffect_TestingEffectWithMultipleEffects() throws IOException, ResourceNotFoundException
	{
		Loadout loadout = loadoutManager.getLoadout(1);
		armourManager.addArmour(WOODCHEST, ArmourSlot.TORSO, loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		legendaryEffectManager.addLegendaryEffect(UNYIELDING, loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO), loadout);
		assertNotNull(loadout.getArmour().getArmourInSlot(ArmourType.ARMOUR, ArmourSlot.TORSO));

		Player player = playerManager.getPlayer(loadout);

		//at level 1, the default HP is 250 - this should result in a special bonus of 1.
		player.setCurrentHP(125);
		playerManager.setSpecial(loadout, Specials.CHARISMA, 5);

		assertEquals(2, player.getSpecials().getSpecialValue(Specials.LUCK, true));
		assertEquals(6, player.getSpecials().getSpecialValue(Specials.CHARISMA, true));
	}
}

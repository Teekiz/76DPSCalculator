package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PerkManagerTest extends BaseTestClass
{
	@Autowired
	PerkManager perkManager;
	@Autowired
	DataLoaderService dataLoaderService;
	@Autowired
	PlayerManager playerManager;

	String STRANGEINNUMBERS;
	String TENDERIZER;
	String GUNSLINGER;

	@BeforeEach
	public void initializeVariables()
	{
		STRANGEINNUMBERS = jsonIDMapper.getIDFromFileName("STRANGEINNUMBERS");
		TENDERIZER = jsonIDMapper.getIDFromFileName("TENDERIZER");
		GUNSLINGER = jsonIDMapper.getIDFromFileName("GUNSLINGER");
	}

	@Test
	void testCanPerkBeAddedDirectly() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testCanPerkBeAddedDirectly in PerkManagerTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		//loadout current specials are 1, which should be enough to add a single perk costing 1 point
		Perk perk = dataLoaderService.loadData(STRANGEINNUMBERS, Perk.class, null);//STRANGEINNUMBERS

		assertTrue(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));
		//by upgrading the perk, it should cost 2 points
		perk.perkRank().setCurrentRank(2);
		assertFalse(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));

		//setting points to 8.
		playerManager.setSpecial(loadout, Specials.CHARISMA, 8);
		assertTrue(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));

		//adding tenderizer which should cost 1 perk point (5 remaining)
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER
		assertTrue(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));

		//adding tenderizer which should cost 3 perk points (0 remaining)
		perkManager.changePerkRank(TENDERIZER, 3, loadout);//TENDERIZER
		assertTrue(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));

		//3 points should already be taken up by tenderizer, so the level 3 card should not have enough points
		perk.perkRank().setCurrentRank(3);
		playerManager.setSpecial(loadout, Specials.CHARISMA, 5);
		assertFalse(perkManager.hasAvailableSpecialPoints(loadout.getPerks(), perk, loadout.getPlayer().getSpecials()));

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	void testCanPerkBeAdded() throws IOException, ResourceNotFoundException {
		log.debug("{}Running test - testCanPerkBeAdded in PerkManagerTest.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER
		assertEquals(1, loadout.getPerks().keySet().size());

		perkManager.addPerk(STRANGEINNUMBERS, loadout);//STRANGEINNUMBERS
		assertEquals(1, loadout.getPerks().keySet().size());

		perkManager.addPerk(GUNSLINGER, loadout);//GUNSLINGER
		assertEquals(2, loadout.getPerks().keySet().size());

		playerManager.setSpecial(loadout, Specials.CHARISMA, 2);
		perkManager.addPerk(STRANGEINNUMBERS, loadout);//STRANGEINNUMBERS
		assertEquals(3, loadout.getPerks().keySet().size());

		//adding duplicate object.
		playerManager.setSpecial(loadout, Specials.CHARISMA, 2);
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER
		assertEquals(3, loadout.getPerks().keySet().size());

		perkManager.changePerkRank(STRANGEINNUMBERS, 2, loadout);//STRANGEINNUMBERS
		//this shouldn't really be used to determine the name, which is why the capitalisation is different
		assertEquals(1, perkManager.getPerkInLoadout(STRANGEINNUMBERS, loadout).perkRank().getCurrentRank());//Strange in Numbers

		playerManager.setSpecial(loadout, Specials.CHARISMA, 3);
		perkManager.changePerkRank(STRANGEINNUMBERS, 2, loadout);//Strange in Numbers
		assertEquals(2, perkManager.getPerkInLoadout(STRANGEINNUMBERS, loadout).perkRank().getCurrentRank());//Strange in Numbers

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

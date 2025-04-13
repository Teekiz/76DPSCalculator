package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.exceptions.ResourceNotFoundException;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class SpecialsEventsChangedTest extends BaseTestClass
{
	@Autowired
	PerkManager perkManager;
	@Autowired
	PlayerManager playerManager;

	//TEST OBJECT IDS
	String STRANGEINNUMBERS;
	String TENDERIZER;
	String HEAVYGUNNER;

	@BeforeEach
	public void initializeVariables()
	{
		STRANGEINNUMBERS = jsonIDMapper.getIDFromFileName("STRANGEINNUMBERS");
		TENDERIZER = jsonIDMapper.getIDFromFileName("TENDERIZER");
		HEAVYGUNNER = jsonIDMapper.getIDFromFileName("HEAVYGUNNER");
	}

	@Test
	public void testSingleSpecial() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testSingleSpecial in SpecialsEventChangedTest.", System.lineSeparator());
		//setting the value to 4.
		Loadout loadout = loadoutManager.getLoadout(1);
		playerManager.setSpecial(loadout, Specials.CHARISMA, 4);
		perkManager.addPerk(STRANGEINNUMBERS, loadout);//STRANGEINNUMBERS
		perkManager.changePerkRank(STRANGEINNUMBERS, 3, loadout);//Strange in Numbers
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout(STRANGEINNUMBERS, loadout).perkRank().getCurrentRank());//Strange in Numbers
		assertEquals(1, perkManager.getPerkInLoadout(TENDERIZER, loadout).perkRank().getCurrentRank());//Tenderizer
		assertEquals(2, loadout.getPerks().size());

		//setting the players charisma to 3, therefore, one of the perks should be removed
		playerManager.setSpecial(loadout, Specials.CHARISMA, 3);
		assertEquals(1, loadout.getPerks().size());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testAllSpecials() throws IOException, ResourceNotFoundException
	{
		log.debug("{}Running test - testAllSpecials in SpecialsEventChangedTest.", System.lineSeparator());
		//setting the value to 4.
		Loadout loadout = loadoutManager.getLoadout(1);
		SpecialDTO specialDTO = new SpecialDTO(3,2,1,4,4,1,1);
		playerManager.setSpecialsFromDTO(loadout, specialDTO);

		perkManager.addPerk(STRANGEINNUMBERS, loadout);//STRANGEINNUMBERS
		perkManager.changePerkRank(STRANGEINNUMBERS, 3, loadout);//Strange in Numbers
		perkManager.addPerk(TENDERIZER, loadout);//TENDERIZER
		perkManager.addPerk(HEAVYGUNNER, loadout);//HEAVYGUNNER
		perkManager.changePerkRank(HEAVYGUNNER, 3, loadout);//Test Heavy Gunner

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout(STRANGEINNUMBERS, loadout).perkRank().getCurrentRank());//Strange in Numbers
		assertEquals(1, perkManager.getPerkInLoadout(TENDERIZER, loadout).perkRank().getCurrentRank());//Tenderizer
		assertEquals(3, perkManager.getPerkInLoadout(HEAVYGUNNER, loadout).perkRank().getCurrentRank());//Test Heavy Gunner
		assertEquals(3, loadout.getPerks().size());

		//setting the players charisma to 3, therefore, one of the perks should be removed
		SpecialDTO newSpecialDTO = new SpecialDTO(2,2,1,3,4,1,1);
		playerManager.setSpecialsFromDTO(loadout, newSpecialDTO);
		assertEquals(1, loadout.getPerks().size());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

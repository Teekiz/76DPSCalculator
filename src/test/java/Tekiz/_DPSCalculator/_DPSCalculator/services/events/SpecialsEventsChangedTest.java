package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
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


	@Test
	public void testSingleSpecial() throws IOException
	{
		log.debug("{}Running test - testSingleSpecial in SpecialsEventChangedTest.", System.lineSeparator());
		//setting the value to 4.
		Loadout loadout = loadoutManager.getLoadout(1);
		playerManager.setSpecial(loadout, Specials.CHARISMA, 4);
		perkManager.addPerk("perks3", loadout);//STRANGEINNUMBERS
		perkManager.changePerkRank("perks3", 3, loadout);//Strange in Numbers
		perkManager.addPerk("perks4", loadout);//TENDERIZER

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout("perks3", loadout).perkRank().getCurrentRank());//Strange in Numbers
		assertEquals(1, perkManager.getPerkInLoadout("perks4", loadout).perkRank().getCurrentRank());//Tenderizer
		assertEquals(2, loadout.getPerks().size());

		//setting the players charisma to 3, therefore, one of the perks should be removed
		playerManager.setSpecial(loadout, Specials.CHARISMA, 3);
		assertEquals(1, loadout.getPerks().size());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@Test
	public void testAllSpecials() throws IOException
	{
		log.debug("{}Running test - testAllSpecials in SpecialsEventChangedTest.", System.lineSeparator());
		//setting the value to 4.
		Loadout loadout = loadoutManager.getLoadout(1);
		SpecialDTO specialDTO = new SpecialDTO(3,2,1,4,4,1,1);
		playerManager.setSpecialsFromDTO(loadout, specialDTO);

		perkManager.addPerk("perks2", loadout);//STRANGEINNUMBERS
		perkManager.changePerkRank("perks2", 3, loadout);//Strange in Numbers
		perkManager.addPerk("perks3", loadout);//TENDERIZER
		perkManager.addPerk("perks4", loadout);//HEAVYGUNNER
		perkManager.changePerkRank("perks4", 3, loadout);//Test Heavy Gunner

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout("perks2", loadout).perkRank().getCurrentRank());//Strange in Numbers
		assertEquals(1, perkManager.getPerkInLoadout("perks3", loadout).perkRank().getCurrentRank());//Tenderizer
		assertEquals(3, perkManager.getPerkInLoadout("perks4", loadout).perkRank().getCurrentRank());//Test Heavy Gunner
		assertEquals(3, loadout.getPerks().size());

		//setting the players charisma to 3, therefore, one of the perks should be removed
		SpecialDTO newSpecialDTO = new SpecialDTO(2,2,1,3,4,1,1);
		playerManager.setSpecialsFromDTO(loadout, newSpecialDTO);
		assertEquals(1, loadout.getPerks().size());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

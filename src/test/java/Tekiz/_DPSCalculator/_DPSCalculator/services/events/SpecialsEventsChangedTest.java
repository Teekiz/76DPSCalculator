package Tekiz._DPSCalculator._DPSCalculator.services.events;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.player.SpecialDTO;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PlayerManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class SpecialsEventsChangedTest
{
	@Autowired
	UserLoadoutTracker userLoadoutTracker;
	@Autowired
	LoadoutManager loadoutManager;
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
		perkManager.addPerk("STRANGEINNUMBERS", loadout);
		perkManager.changePerkRank("Strange in Numbers", 3, loadout);
		perkManager.addPerk("TENDERIZER", loadout);

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout("Strange in Numbers", loadout).perkRank().getCurrentRank());
		assertEquals(1, perkManager.getPerkInLoadout("Tenderizer", loadout).perkRank().getCurrentRank());
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

		perkManager.addPerk("STRANGEINNUMBERS", loadout);
		perkManager.changePerkRank("Strange in Numbers", 3, loadout);
		perkManager.addPerk("TENDERIZER", loadout);
		perkManager.addPerk("HEAVYGUNNER", loadout);
		perkManager.changePerkRank("Test Heavy Gunner", 3, loadout);

		//ensuring that each perk has been added and that all four spaces are used.
		assertEquals(3, perkManager.getPerkInLoadout("Strange in Numbers", loadout).perkRank().getCurrentRank());
		assertEquals(1, perkManager.getPerkInLoadout("Tenderizer", loadout).perkRank().getCurrentRank());
		assertEquals(3, perkManager.getPerkInLoadout("Test Heavy Gunner", loadout).perkRank().getCurrentRank());
		assertEquals(3, loadout.getPerks().size());

		//setting the players charisma to 3, therefore, one of the perks should be removed
		SpecialDTO newSpecialDTO = new SpecialDTO(2,2,1,3,4,1,1);
		playerManager.setSpecialsFromDTO(loadout, newSpecialDTO);
		assertEquals(1, loadout.getPerks().size());

		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

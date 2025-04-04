package Tekiz._DPSCalculator._DPSCalculator.services.serialization;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.ConsumableManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LegendaryEffectManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.MutationManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.PerkManager;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.WeaponManager;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class TestRedisSerialization extends BaseTestClass
{
	@Autowired
	WeaponManager weaponManager;
	@Autowired
	PerkManager perkManager;
	@Autowired
	ConsumableManager consumableManager;
	@Autowired
	MutationManager mutationManager;
	@Autowired
	LegendaryEffectManager legendaryEffectManager;

	String _10MMPISTOL;
	String CALIBRATE;
	String FURY;
	String BALLISTICBOCK;
	String GUNSLINGER;
	String STRANGEINNUMBERS;
	String ADRENALREACTION;
	String ANTIARMOUR;


	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
		FURY = jsonIDMapper.getIDFromFileName("FURY");
		BALLISTICBOCK = jsonIDMapper.getIDFromFileName("BALLISTICBOCK");
		GUNSLINGER = jsonIDMapper.getIDFromFileName("GUNSLINGER");
		STRANGEINNUMBERS = jsonIDMapper.getIDFromFileName("STRANGEINNUMBERS");
		ADRENALREACTION = jsonIDMapper.getIDFromFileName("ADRENALREACTION");
		ANTIARMOUR = jsonIDMapper.getIDFromFileName("ANTIARMOUR");
	}

	@Test
	public void testRedisSerialization() throws IOException
	{
		log.debug("{}Running test - testRedisSerialization in TestRedisSerialization.", System.lineSeparator());
		Loadout loadout = loadoutManager.getLoadout(1);
		weaponManager.setWeapon(_10MMPISTOL, loadout);//10MMPISTOL
		weaponManager.modifyWeapon(CALIBRATE, loadout);//CALLIBRATE
		consumableManager.addConsumable(FURY, loadout);//FURY
		consumableManager.addConsumable(BALLISTICBOCK, loadout);//BALLISTICBOCK
		perkManager.addPerk(GUNSLINGER, loadout);//GUNSLINGER
		perkManager.addPerk(STRANGEINNUMBERS, loadout);//STRANGEINNUMBERS
		mutationManager.addMutation(ADRENALREACTION, loadout);//ADRENALREACTION
		legendaryEffectManager.addLegendaryEffect(ANTIARMOUR, loadout.getWeapon(), loadout);//ANTIARMOUR

		loadout = null;

		Loadout newLoadout = loadoutManager.getLoadout(1);
		assertNotNull(newLoadout);

		assertNotNull(newLoadout.getWeapon());
		assertEquals("Test 10mm pistol", newLoadout.getWeapon().getName());

		log.debug("Loadout: {}", loadout);
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

}

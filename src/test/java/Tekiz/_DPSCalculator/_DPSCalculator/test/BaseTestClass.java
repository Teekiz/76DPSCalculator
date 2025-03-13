package Tekiz._DPSCalculator._DPSCalculator.test;

import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class BaseTestClass
{
	@Autowired
	protected LoadoutManager loadoutManager;
	@Autowired
	protected UserLoadoutTracker userLoadoutTracker;

	@BeforeEach
	public void setUp()
	{
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}

	@AfterEach
	public void cleanUp()
	{
		loadoutManager.deleteAllLoadouts(userLoadoutTracker.getSessionID());
	}
}

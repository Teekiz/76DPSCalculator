package Tekiz._DPSCalculator._DPSCalculator.test;

import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.jsonidmapper.JsonIDMapper;

@SpringBootTest
public abstract class BaseTestClass
{
	@Autowired
	protected LoadoutManager loadoutManager;
	@Autowired
	protected UserLoadoutTracker userLoadoutTracker;
	@Autowired
	protected JsonIDMapper jsonIDMapper;

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

	public double round(double value)
	{
		try {
			BigDecimal bigDecimal = BigDecimal.valueOf(value);
			return bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
		} catch (Exception e){
			return value;
		}
	}
}

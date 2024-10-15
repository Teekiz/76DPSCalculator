package Tekiz._DPSCalculator._DPSCalculator.aspect;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.manager.LoadoutManager;
import Tekiz._DPSCalculator._DPSCalculator.services.session.UserLoadoutTracker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
public class LoadoutAspect
{
	private final UserLoadoutTracker userLoadoutTracker;
	private final LoadoutManager loadoutManager;
	private final boolean save = true;
	@Autowired
	public LoadoutAspect(UserLoadoutTracker userLoadoutTracker, LoadoutManager loadoutManager)
	{
		this.userLoadoutTracker = userLoadoutTracker;
		this.loadoutManager = loadoutManager;
	}
	@After("@annotation(SaveLoadout)")
	public void saveLoadout(JoinPoint joinPoint)
	{
		if (save)
		{
			Object[] objects = joinPoint.getArgs();

			for (Object object : objects)
			{
				if (object instanceof Loadout)
				{
					Loadout loadout = (Loadout) object;
					loadoutManager.saveActiveLoadout(userLoadoutTracker.getSessionID(), loadout);
					log.debug("Saving loadout {} after method execution.", loadout.getLoadoutID());
					break;
				}
			}
		}
	}
}

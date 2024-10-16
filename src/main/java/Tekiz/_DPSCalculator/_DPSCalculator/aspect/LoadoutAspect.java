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

/**
 * An aspect to handle saving when methods marked by {@link SaveLoadout} are called.
 */
@Slf4j
@Aspect
@Component
public class LoadoutAspect
{
	private final UserLoadoutTracker userLoadoutTracker;
	private final LoadoutManager loadoutManager;
	//todo - change to use from config
	private final boolean save = true;

	/**
	 * The constructor for a {@link LoadoutAspect} object.
	 * @param userLoadoutTracker A service to retrieve the session ID for the user.
	 * @param loadoutManager A manager that is used to save the loadout object.
	 */
	@Autowired
	public LoadoutAspect(UserLoadoutTracker userLoadoutTracker, LoadoutManager loadoutManager)
	{
		this.userLoadoutTracker = userLoadoutTracker;
		this.loadoutManager = loadoutManager;
	}

	/**
	 * An aspect method that saves the loadout after the execution of a method
	 * annotated with {@link @SaveLoadout}. The method checks if the `save` flag is set to true,
	 * and if a {@link  Loadout} object is passed as one of the method arguments, it saves the
	 * active loadout using the {@code loadoutManager} and the session id from the {@link UserLoadoutTracker}.
	 * @param joinPoint The {@link JoinPoint} representing the method execution, which provides
	 *                  access to the method's arguments for inspection.
	 */
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

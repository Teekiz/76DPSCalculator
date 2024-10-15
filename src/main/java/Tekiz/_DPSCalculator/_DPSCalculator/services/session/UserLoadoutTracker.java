package Tekiz._DPSCalculator._DPSCalculator.services.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Slf4j
@Component
@SessionScope
public class UserLoadoutTracker
{
	//todo - at some point his probably won't be needed, so this may be deleted.
	private final String sessionID;
	@Autowired
	public UserLoadoutTracker(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		this.sessionID = session != null ? session.getId() : null;
		log.debug("Creating new UserLoadoutTracker for session ID: {}.", sessionID);
	}
	public synchronized String getSessionID()
	{
		return sessionID;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.services.session;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A component used to retrieve the users session ID.
 */
@Slf4j
@Component
public class UserLoadoutTracker
{
	@Autowired
	private HttpServletRequest request;
	public synchronized String getSessionID()
	{
		return request.getSession().getId();
	}
}

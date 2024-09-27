package Tekiz._DPSCalculator._DPSCalculator.services.scripts;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroovyScriptService
{
	private final GroovyShell groovyShell;
	private static final Logger logger = LoggerFactory.getLogger(GroovyScriptService.class);

	@Autowired
	public GroovyScriptService(GroovyShell groovyShell)
	{
		this.groovyShell = groovyShell;
	}

	public Script parseFile(File scriptFile) throws IOException
	{
		return groovyShell.parse(scriptFile);
	}

	public Object runMethod(Script script, String methodName, Number... values) {
		try {
			return script.invokeMethod(methodName, values);
		} catch (ClassCastException e) {
			logger.error("Type casting error encountered while executing method '{}': {}", methodName, e.getMessage());
			return null;
		} catch (MissingMethodExceptionNoStack e) {
			logger.error("Could not find method: {} with arguments {}", methodName, values, e);
			return null;
		} catch (Exception e) {
			logger.error("Unexpected error during script execution for method: {}",methodName, e);
			return null;
		}
	}
}

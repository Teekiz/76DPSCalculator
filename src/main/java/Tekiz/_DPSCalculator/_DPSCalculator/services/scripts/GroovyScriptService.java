package Tekiz._DPSCalculator._DPSCalculator.services.scripts;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service for handling Groovy script parsing and execution.
 * This service allows for the loading of Groovy scripts from files and invoking
 * specific methods within the loaded scripts.
 *
 * <p>This service can be used to extend application logic dynamically by executing Groovy scripts,
 * enabling flexibility and customization for specific behaviors in runtime.</p>
 */
@Service
@Slf4j
public class GroovyScriptService
{
	private final GroovyShell groovyShell;

	/**
	 * The constructor for a {@link GroovyScriptService} object.
	 * @param groovyShell The GroovyShell instance used for parsing and executing scripts.
	 */
	@Autowired
	public GroovyScriptService(GroovyShell groovyShell)
	{
		this.groovyShell = groovyShell;
	}

	/**
	 * A method that parses a Groovy script file and returns the {@link Script} object.
	 * @param scriptFile The file containing the Groovy script to be parsed.
	 * @return The parsed {@link Script} object representing the Groovy script.
	 * @throws IOException If an error occurs while reading the file.
	 */
	public Script parseFile(File scriptFile) throws IOException
	{
		return groovyShell.parse(scriptFile);
	}

	/**
	 * A method that invokes a given method of {@code methodName} in the {@code script} object.
	 * @param script The {@link Script} object containing the groovy code.
	 * @param methodName The name of the method to be invoked.
	 * @param values The arguments to be passed to the method.
	 * @return The result of the method invocation, or {@code null} if an error occurs.
	 */
	public Object runMethod(Script script, String methodName, Number... values) {
		try {
			return script.invokeMethod(methodName, values);
		} catch (ClassCastException e) {
			log.error("Type casting error encountered while executing method '{}': {}", methodName, e.getMessage());
			return null;
		} catch (MissingMethodExceptionNoStack e) {
			log.error("Could not find method: {} with arguments {} using script: {}", methodName, values, script, e);
			return null;
		} catch (Exception e) {
			log.error("Unexpected error during script execution for method: {} using script: {}", methodName, script, e);
			return null;
		}
	}
}

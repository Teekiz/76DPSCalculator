package Tekiz._DPSCalculator._DPSCalculator.services.scripts;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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
	 * A method that invokes a given method in the {@code script} object.
	 * @param scriptFile The {@link File}  containing the groovy code.
	 * @param loadout The arguments to be passed to the method.
	 * @return The result of the method invocation, or {@code null} if an error occurs.
	 */
	public Map.Entry<ModifierTypes, ModifierValue<?>> runMethod(File scriptFile, Loadout loadout) throws IOException
	{
		Script script = parseFile(scriptFile);
		if (script == null){
			log.error("Failed to parse script file: {}", scriptFile.getAbsolutePath());
			return null;
		}

		try {
			Object result = script.invokeMethod("run", loadout);

			if (result instanceof Map.Entry<?, ?> entry) {
				if (entry.getKey() instanceof ModifierTypes && entry.getValue() instanceof ModifierValue<?>) {
					return (Map.Entry<ModifierTypes, ModifierValue<?>>) entry;
				} else {
					log.error("Script returned invalid types: Key={}, Value={}",
						entry.getKey().getClass(), entry.getValue().getClass());
					return null;
				}
			} else {
				log.error("Script did not return a Map.Entry but instead: {}", result.getClass().getName());
				return null;
			}
		} catch (ClassCastException e) {
			log.error("Type casting error encountered while executing method '{}': {}", "run", e.getMessage());
			return null;
		} catch (MissingMethodException e) {
			log.error("Could not find method: {} with arguments {} using script: {}", "run", loadout, script, e);
			return null;
		} catch (Exception e) {
			log.error("Unexpected error during script execution for method: {} using script: {}", "run", script, e);
			return null;
		}
	}

	/**
	 * A method that parses a Groovy script file and returns the {@link Script} object.
	 * @param scriptFile The file containing the Groovy script to be parsed.
	 * @return The parsed {@link Script} object representing the Groovy script.
	 * @throws IOException If an error occurs while reading the file.
	 */
	private Script parseFile(File scriptFile) throws IOException
	{
		return groovyShell.parse(scriptFile);
	}
}

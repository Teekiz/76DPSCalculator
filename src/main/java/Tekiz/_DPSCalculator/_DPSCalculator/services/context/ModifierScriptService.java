package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierValue;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ScriptLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import java.io.File;
import java.io.IOException;
import groovy.lang.Script;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to store Groovy {@link Script}s to run dynamic methods at runtime.
 *
 * <p>Expressions stored by the {@link ModifierExpressionService} can call this service to run additional methods if required by the {@link Modifier}.</p>
 */
@Slf4j
@Service
public class ModifierScriptService
{
	//This service is used to run conditions programmatically that SPeL can call, useful for complex conditions that SPeL can't handle
	private final GroovyScriptService groovyScriptService;
	private final ScriptLoaderService scriptLoaderService;

	/**
	 * The constructor for a {@link ModifierScriptService} object.
	 * @param groovyScriptService A service for handling Groovy script parsing and execution.
	 * @param scriptLoaderService A service for loading script files.
	 * @throws IOException
	 */
	@Autowired
	public ModifierScriptService(GroovyScriptService groovyScriptService, ScriptLoaderService scriptLoaderService) throws IOException
	{
		this.groovyScriptService = groovyScriptService;
		this.scriptLoaderService = scriptLoaderService;
	}

	/**
	 * A method for running a method of the given {@code methodName} contained within {@code methodScripts}.
	 *
	 * <p>Expressions stored by the {@link ModifierExpressionService} can call this method to run additional methods if required by the {@link Modifier}.</p>
	 *
	 * @param scriptName The name of the method to be invoked.
	 * @param loadout The loadout to be checked.
	 * @return The result of the method invocation, or {@code null} if an error occurs.
	 */
	public Map.Entry<ModifierTypes, ModifierValue<?>> getAdditionalContext(String scriptName, Loadout loadout)
	{
		try
		{
			File file = scriptLoaderService.getScriptFile(scriptName);

			if (file == null || !file.exists()){
				log.error("Cannot find script files!");
				return null;
			}

			String fileName = file.getName();
			int dotIndex = fileName.lastIndexOf('.');
			String extension = (dotIndex > 0) ? fileName.substring(dotIndex + 1).toLowerCase() : "";

			switch (extension.toLowerCase()) {
				case "groovy" -> {
					return groovyScriptService.runMethod(file, loadout);
				}
				case "py" -> {
					return null;
				}
				default ->
				{
					return null;
				}
			}
		}
		catch (Exception e)
		{
			log.debug("Error using script.", e);
			return null;
		}
	}
}

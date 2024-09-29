package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModifierMethodLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import Tekiz._DPSCalculator._DPSCalculator.model.modifiers.Modifier;
import java.io.IOException;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to store Groovy {@link Script}s to run dynamic methods at runtime.
 *
 * <p>Expressions stored by the {@link ModifierExpressionService} can call this service to run additional methods if required by the {@link Modifier}.</p>
 */
@Service
public class ModifierMethodService
{
	//This service is used to run conditions programmatically that SPeL can call, useful for complex conditions that SPeL can't handle
	private final GroovyScriptService scriptService;
	private final Script methodScripts;

	/**
	 * The constructor for a {@link ModifierMethodService} object.
	 * @param scriptService A service for handling Groovy script parsing and execution.
	 * @param modifierMethodLoaderService A service for loading script files.
	 * @throws IOException
	 */
	@Autowired
	public ModifierMethodService(GroovyScriptService scriptService, ModifierMethodLoaderService modifierMethodLoaderService) throws IOException
	{
		this.scriptService = scriptService;
		this.methodScripts = modifierMethodLoaderService.loadMethodScript();
	}

	/**
	 * A method for running a method of the given {@code methodName} contained within {@code methodScripts}.
	 *
	 * <p>Expressions stored by the {@link ModifierExpressionService} can call this method to run additional methods if required by the {@link Modifier}.</p>
	 *
	 * @param methodName The name of the method to be invoked.
	 * @param value The arguments to be passed to the method.
	 * @return The result of the method invocation, or {@code null} if an error occurs.
	 */
	public Object runMethod(String methodName, Number... value)
	{
		if (methodScripts != null)
		{
			return scriptService.runMethod(methodScripts, methodName, value);
		}
		return null;
	}
}

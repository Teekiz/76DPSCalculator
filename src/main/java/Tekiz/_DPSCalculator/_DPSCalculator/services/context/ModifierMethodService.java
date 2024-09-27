package Tekiz._DPSCalculator._DPSCalculator.services.context;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModifierMethodLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import java.io.IOException;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierMethodService
{
	//This service is used to run conditions programmatically that SPeL can call, useful for complex conditions that SPeL can't handle

	private final GroovyScriptService scriptService;
	private final Script methodScripts;

	@Autowired
	public ModifierMethodService(GroovyScriptService scriptService, ModifierMethodLoaderService modifierMethodLoaderService) throws IOException
	{
		this.scriptService = scriptService;
		this.methodScripts = modifierMethodLoaderService.loadMethodScript();
	}

	public Object runMethod(String methodName, Number... value)
	{
		if (methodScripts != null)
		{
			return scriptService.runMethod(methodScripts, methodName, value);
		}
		return null;
	}
}

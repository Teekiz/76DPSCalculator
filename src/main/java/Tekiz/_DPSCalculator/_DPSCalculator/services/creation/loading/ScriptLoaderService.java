package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import groovy.lang.Script;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScriptLoaderService
{
	private final GroovyScriptService scriptService;
	private final File modifierMethodScriptFile;

	//todo - handle null pointer exceptions for missing data

	@Autowired
	public ScriptLoaderService(GroovyScriptService scriptService, FileConfig fileConfig)
	{
		this.scriptService = scriptService;
		this.modifierMethodScriptFile = new File(fileConfig.getPaths().get("methodScript"));
	}

	public Script loadMethodScript()
	{
		try {
			return scriptService.parseFile(modifierMethodScriptFile);
		}
		catch (Exception e) {
			log.error("Unexpected error while loading script: {}", e.getMessage(), e);
			return null;
		}
	}
}

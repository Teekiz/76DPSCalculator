package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.lang.Script;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierMethodLoaderService
{
	private final ObjectMapper objectMapper;
	private final GroovyScriptService scriptService;
	private final File modifierMethodScriptFile;
	private static final Logger logger = LoggerFactory.getLogger(ModifierMethodLoaderService.class);

	//todo - handle null pointer exceptions for missing data

	@Autowired
	public ModifierMethodLoaderService(ObjectMapper objectMapper, GroovyScriptService scriptService, FileConfig fileConfig)
	{
		this.objectMapper = objectMapper;
		this.scriptService = scriptService;
		this.modifierMethodScriptFile = new File(fileConfig.getPaths().get("methodScript"));
	}

	public Script loadMethodScript()
	{
		try {
			return scriptService.parseFile(modifierMethodScriptFile);
		}
		catch (Exception e) {
			logger.error("Unexpected error while loading script: {}", e.getMessage(), e);
			return null;
		}
	}
}

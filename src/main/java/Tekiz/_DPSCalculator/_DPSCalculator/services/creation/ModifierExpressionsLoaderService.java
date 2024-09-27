package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.services.parser.ParsingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

@Service
public class ModifierExpressionsLoaderService
{
	private final ObjectMapper objectMapper;
	private final ParsingService ParsingService;
	private final File modifierContextFile;
	private static final Logger logger = LoggerFactory.getLogger(ModifierExpressionsLoaderService.class);


	@Autowired
	public ModifierExpressionsLoaderService(ObjectMapper objectMapper, ParsingService ParsingService, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.ParsingService = ParsingService;
		this.modifierContextFile = new File(fileConfig.getPaths().get("modifierExpression"));
	}

	public HashMap<String, Expression> getContextInformation() throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(modifierContextFile);
		HashMap<String, Expression> contextHashmap = new HashMap<>();
		Iterator<String> fieldNames = rootNode.fieldNames();
		while (fieldNames.hasNext())
		{
			String contextName = fieldNames.next();
			JsonNode childNode = rootNode.get(contextName);
			String expressionString = childNode.get("expressionString").asText();
			try {
				// attempt to parse the expression
				Expression contextExpression = ParsingService.parseString(expressionString);
				contextHashmap.put(contextName, contextExpression);
			} catch (ParseException e) {
				logger.error("Failed to parse expression for context '{}': {}", contextName, e.getMessage());
			} catch (Exception e) {
				logger.error("Unexpected error while processing context '{}': {}", contextName, e.getMessage());
			}
		}
		return contextHashmap;
	}
}

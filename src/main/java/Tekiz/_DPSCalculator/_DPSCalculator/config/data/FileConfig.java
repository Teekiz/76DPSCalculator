package Tekiz._DPSCalculator._DPSCalculator.config.data;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig
{
	//todo - change this so that its a bit more organised
	@Bean
	public ExpressionParser expressionParser()
	{
		return new SpelExpressionParser();
	}

	private Map<String, String> paths;
	public Map<String, String> getPaths() {
		return paths;
	}
	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.config.data;

import Tekiz._DPSCalculator._DPSCalculator.util.binding.BaseBinding;
import groovy.lang.GroovyShell;
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
	//todo - reduce coupling between this file and the loaders
	@Bean
	public ExpressionParser expressionParser()
	{
		return new SpelExpressionParser();
	}
	@Bean
	public GroovyShell groovyShell() {return new GroovyShell(BaseBinding.getBaseBinding());}
	private Map<String, String> paths;
	public Map<String, String> getPaths() {
		return paths;
	}
	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}
}

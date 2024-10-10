package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.util.binding.BaseBinding;
import groovy.lang.GroovyShell;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class UtilConfig
{
	@Bean
	public ExpressionParser expressionParser()
	{
		return new SpelExpressionParser();
	}
	@Bean
	public GroovyShell groovyShell() {return new GroovyShell(BaseBinding.getBaseBinding());}
}

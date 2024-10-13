package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.util.binding.BaseBinding;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import groovy.lang.GroovyShell;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

	@Bean
	@Primary
	public ObjectMapper objectMapper(ModLoaderService modLoaderService, WeaponFactory weaponFactory,
									 LoadoutFactory loadoutFactory) {
		ObjectMapper objectMapper = new  ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		InjectableValues.Std injectableValues = new InjectableValues.Std();
		injectableValues.addValue(ModLoaderService.class, modLoaderService);
		injectableValues.addValue(WeaponFactory.class, weaponFactory);
		injectableValues.addValue(LoadoutFactory.class, loadoutFactory);
		objectMapper.setInjectableValues(injectableValues);

		return objectMapper;
	}
}

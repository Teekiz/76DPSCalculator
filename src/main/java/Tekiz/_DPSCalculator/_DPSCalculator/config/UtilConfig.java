package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.util.binding.BaseBinding;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.LoadoutDeserializer;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ReceiverDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
	public ObjectMapper objectMapper(LoadoutDeserializer loadoutDeserializer,
									 ReceiverDeserializer receiverDeserializer) {
		ObjectMapper objectMapper = new ObjectMapper()
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.registerModule(new SimpleModule()
			.addDeserializer(Loadout.class, loadoutDeserializer)
			.addDeserializer(Receiver.class, receiverDeserializer));
		return objectMapper;
	}
}

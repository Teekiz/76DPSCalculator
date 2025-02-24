package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.LoadoutFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.util.binding.BaseBinding;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ExpressionComponent;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.HashMapKeyComponent;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.LoadoutDeserializer;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import groovy.lang.GroovyShell;
import org.jsonidmapper.JsonIDMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.expression.Expression;
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

	@Value("${files.path.properties}")
	private String filePathProperties;
	@Value("${storage.path.properties}")
	private String storagePathProperties;

	@Bean
	public JsonIDMapper jsonIDMapper() {return new JsonIDMapper(filePathProperties,
		storagePathProperties, false);}

	@Bean
	@Primary
	public ObjectMapper objectMapper(DataLoaderService loaderService, WeaponFactory weaponFactory,
									 LoadoutFactory loadoutFactory) {
		ObjectMapper objectMapper = new  ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		InjectableValues.Std injectableValues = new InjectableValues.Std();
		injectableValues.addValue(DataLoaderService.class, loaderService);
		injectableValues.addValue(WeaponFactory.class, weaponFactory);
		injectableValues.addValue(LoadoutFactory.class, loadoutFactory);
		objectMapper.setInjectableValues(injectableValues);

		HashMapKeyComponent.HashMapKeySerializer hashMapKeySerializer = new HashMapKeyComponent.HashMapKeySerializer();
		HashMapKeyComponent.HashMapKeyDeserializer hashMapKeyDeserializer = new HashMapKeyComponent.HashMapKeyDeserializer();
		ExpressionComponent.ExpressionSerializer expressionSerializer = new ExpressionComponent.ExpressionSerializer();
		ExpressionComponent.ExpressionDeserializer expressionDeserializer = new ExpressionComponent.ExpressionDeserializer();
		LoadoutDeserializer loadoutDeserializer = new LoadoutDeserializer();

		SimpleModule module = new SimpleModule();
		module.addKeySerializer(Perk.class, hashMapKeySerializer);
		module.addKeySerializer(Consumable.class, hashMapKeySerializer);
		module.addKeySerializer(Mutation.class, hashMapKeySerializer);

		module.addKeyDeserializer(Perk.class, hashMapKeyDeserializer);
		module.addKeyDeserializer(Consumable.class, hashMapKeyDeserializer);
		module.addKeyDeserializer(Mutation.class, hashMapKeyDeserializer);

		module.addSerializer(Expression.class, expressionSerializer);
		module.addDeserializer(Expression.class, expressionDeserializer);

		module.addDeserializer(Loadout.class, loadoutDeserializer);

		objectMapper.registerModule(module);

		return objectMapper;
	}
}

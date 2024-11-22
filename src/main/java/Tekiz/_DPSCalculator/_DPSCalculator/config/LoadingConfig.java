package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.JsonLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsonidmapper.JsonIDMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class LoadingConfig
{
	@Lazy
	@Bean
	public ObjectLoaderStrategy jsonLoaderStrategy(ObjectMapper objectMapper, JsonIDMapper jsonIDMapper) {
		return new JsonLoaderStrategy(objectMapper, jsonIDMapper);
	}
}

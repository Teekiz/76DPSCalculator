package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.JsonLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.MongoDBLoaderStrategy;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsonidmapper.JsonIDMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class LoadingConfig
{
	@Bean
	@Lazy
	@Profile({"local-tests", "container-tests", "local"})
	public ObjectLoaderStrategy jsonLoaderStrategy(ObjectMapper objectMapper, JsonIDMapper jsonIDMapper) {
		log.debug("Using JSON loader strategy.");
		return new JsonLoaderStrategy(objectMapper, jsonIDMapper);
	}

	@Bean
	@Lazy
	@Profile("docker")
	public ObjectLoaderStrategy dbLoaderStrategy(ApplicationContext applicationContext) {
		log.debug("Using Mongo loader strategy.");
		return new MongoDBLoaderStrategy(applicationContext);
	}
}

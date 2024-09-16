package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScopeConfig
{
	@Bean
	public LoadoutScope loadoutScope() {
		return new LoadoutScope();
	}

	@Bean
	public static BeanFactoryPostProcessor scopeRegistrar() {
		return beanFactory -> {
			beanFactory.registerScope("loadout", beanFactory.getBean(LoadoutScope.class));
		};
	}
}

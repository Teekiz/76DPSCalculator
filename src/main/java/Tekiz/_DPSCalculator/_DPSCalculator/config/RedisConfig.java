package Tekiz._DPSCalculator._DPSCalculator.config;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisConfig {

	@Bean
	public LettuceConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Loadout> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Loadout> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Loadout.class));
		return template;
	}
}

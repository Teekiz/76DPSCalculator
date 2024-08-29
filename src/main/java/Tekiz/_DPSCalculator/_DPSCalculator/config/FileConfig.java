package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfig
{
	@Value("${weapon.data.file.path}")
	private String weaponDataFilePath;

	@Bean
	public String weaponDataFilePath() {
		return weaponDataFilePath;
	}

	@Value("${receiver.data.file.path}")
	private String receiverDataFilePath;

	@Bean
	public String receiverDataFilePath() {
		return receiverDataFilePath;
	}
}

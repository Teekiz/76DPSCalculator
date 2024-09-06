package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModFileConfig
{
	@Value("${receiver.data.file.path}")
	private String receiverDataFilePath;

	@Bean
	public String receiverDataFilePath() {
		return receiverDataFilePath;
	}

	@Value("${armourMaterial.data.file.path}")
	private String armourMaterialDataFilePath;

	@Bean
	public String armourMaterialDataFilePath() {
		return armourMaterialDataFilePath;
	}

	@Value("${armourMisc.data.file.path}")
	private String armourMiscDataFilePath;

	@Bean
	public String armourMiscDataFilePath() {
		return armourMiscDataFilePath;
	}
}

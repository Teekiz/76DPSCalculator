package Tekiz._DPSCalculator._DPSCalculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
@Configuration
public class FileConfig
{
	@Bean
	public ExpressionParser expressionParser()
	{
		return new SpelExpressionParser();
	}

	@Value("${weapon.data.file.path}")
	private String weaponDataFilePath;

	@Bean
	public String weaponDataFilePath() {
		return weaponDataFilePath;
	}

	@Value("${perk.data.file.path}")
	private String perkDataFilePath;

	@Bean
	public String perkDataFilePath() {
		return perkDataFilePath;
	}

	@Value("${consumable.data.file.path}")
	private String consumableDataFilePath;

	@Bean
	public String consumableDataFilePath() {
		return consumableDataFilePath;
	}

	@Value("${armour.data.file.path}")
	private String armourDataFilePath;

	@Bean
	public String armourDataFilePath() {
		return armourDataFilePath;
	}
}

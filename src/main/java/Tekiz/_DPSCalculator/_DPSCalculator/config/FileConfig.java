package Tekiz._DPSCalculator._DPSCalculator.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig
{
	//todo - reduce coupling between this file and the loaders
	private Map<String, String> paths;

}

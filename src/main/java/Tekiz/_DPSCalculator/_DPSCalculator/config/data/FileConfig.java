package Tekiz._DPSCalculator._DPSCalculator.config.data;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileConfig
{
	//todo - reduce coupling between this file and the loaders
	private Map<String, String> paths;
	public Map<String, String> getPaths() {
		return paths;
	}
	public void setPaths(Map<String, String> paths) {
		this.paths = paths;
	}
}

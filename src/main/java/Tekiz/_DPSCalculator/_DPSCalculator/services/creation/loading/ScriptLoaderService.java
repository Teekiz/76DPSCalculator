package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.services.scripts.GroovyScriptService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScriptLoaderService
{
	private final HashMap<String, File> modifierMethodScriptFile;

	@Autowired
	public ScriptLoaderService(FileConfig fileConfig) throws IOException
	{
		this.modifierMethodScriptFile = getModifierMethodScriptFiles(fileConfig.getPaths().get("scripts"));
	}

	/**
	 * A method that loads all script files within the provided repository.
	 * @param directoryPath The directory containing all script files.
	 * @return A {@link HashMap} containing the file name (without the extension) and the file itself.
	 * @throws IOException
	 */
	private HashMap<String, File> getModifierMethodScriptFiles(String directoryPath) throws IOException
	{
		HashMap<String, File> fileHashMap = new HashMap<>();

		try (Stream<Path> stream = Files.walk(Paths.get(directoryPath))) {
			stream
				.filter(Files::isRegularFile)
				.forEach(path -> {
					String fileName = path.getFileName().toString();
					String nameWithoutExtension = fileName.contains(".")
						? fileName.substring(0, fileName.lastIndexOf('.'))
						: fileName;
					fileHashMap.put(nameWithoutExtension.toLowerCase(), path.toFile());
				});
		}

		return fileHashMap;
	}

	/**
	 * A method to get a script file based on the provided {@code scriptName}
	 * @param scriptName The name of the file to be returned.
	 * @return The script {@link File}.
	 */
	public File getScriptFile(String scriptName)
	{
		return modifierMethodScriptFile.get(scriptName.toLowerCase());
	}
}

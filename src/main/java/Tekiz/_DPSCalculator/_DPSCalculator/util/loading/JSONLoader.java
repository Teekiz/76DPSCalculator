package Tekiz._DPSCalculator._DPSCalculator.util.loading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility object to provide loader services a tool to get all .JSON files within a directory.
 */
public class JSONLoader
{
	/** The extension for the file. */
	static String fileType = ".json";

	/**
	 * A method to find a single JSON file within a directory.
	 * @param directory The root directory to be searched.
	 * @param fileName The name of the file. Do not include ".json".
	 * @return The .JSON file if found or {@code null} if no file of that name can be found.
	 * @throws IOException If the method cannot read the file.
	 */
	public static File getJSONFile(File directory, String fileName) throws IOException
	{
		try(Stream<Path> walk = Files.walk(directory.toPath()))
		{
			return walk
				.filter(Files::isRegularFile)
				.filter(path -> path.getFileName().toString().equalsIgnoreCase(fileName + fileType))
				.map(Path::toFile)
				.findFirst()
				.orElse(null);
		}
	}

	/**
	 * A method to find all JSON files in a directory
	 * @param directory The root directory to be searched.
	 * @return All .JSON files found within the directory.
	 * @throws IOException If the method cannot read the file.
	 */
	public static List<File> getAllJSONFiles(File directory) throws IOException
	{
		try(Stream<Path> walk = Files.walk(directory.toPath()))
		{
			return walk
				.filter(Files::isRegularFile)
				.filter(path -> path.toString().endsWith(fileType))
				.map(Path::toFile)
				.collect(Collectors.toList());
		}
	}
}

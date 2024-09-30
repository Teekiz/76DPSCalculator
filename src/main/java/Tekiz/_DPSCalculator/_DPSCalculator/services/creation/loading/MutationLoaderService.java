package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.config.data.FileConfig;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.util.loading.JSONLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MutationLoaderService
{
	private final ObjectMapper objectMapper;
	private final File mutationFile;

	@Autowired
	public MutationLoaderService(ObjectMapper objectMapper, FileConfig fileConfig) throws UnsatisfiedDependencyException
	{
		this.objectMapper = objectMapper;
		this.mutationFile = new File(fileConfig.getPaths().get("mutation"));
	}

	public Mutation getMutation(String mutationName) throws IOException
	{
		File jsonFile = JSONLoader.getJSONFile(mutationFile, mutationName);
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		return objectMapper.treeToValue(rootNode, Mutation.class);
	}

	public List<Mutation> getAllMutations() throws IOException
	{
		List<Mutation> mutations = new ArrayList<>();
		List<File> jsonFiles = JSONLoader.getAllJSONFiles(mutationFile);
		for (File file : jsonFiles)
		{
			JsonNode rootNode = objectMapper.readTree(file);
			if (rootNode != null)
			{
				mutations.add(objectMapper.treeToValue(rootNode, Mutation.class));
			}
		}
		return mutations;
	}
}

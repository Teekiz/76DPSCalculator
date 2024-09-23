package Tekiz._DPSCalculator._DPSCalculator.services.creation;

import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MutationLoaderService
{
	private final ObjectMapper objectMapper;
	private final File mutationFile;

	@Autowired
	public MutationLoaderService(ObjectMapper objectMapper, @Value("${mutation.data.file.path}") String mutationFile)
	{
		this.objectMapper = objectMapper;
		this.mutationFile = new File(mutationFile);
	}

	public Mutation getMutation(String mutationName) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(mutationFile);
		JsonNode mutationNode = rootNode.get(mutationName.toUpperCase());
		return objectMapper.treeToValue(mutationNode, Mutation.class);
	}

	public List<Mutation> getAllMutations() throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(mutationFile);
		List<Mutation> mutations = new ArrayList<>();
		Iterator<JsonNode> elements = rootNode.elements();

		while (elements.hasNext()) {
			JsonNode mutationNode = elements.next();
			Mutation mutation = objectMapper.treeToValue(mutationNode, Mutation.class);
			mutations.add(mutation);
		}
		return mutations;
	}
}

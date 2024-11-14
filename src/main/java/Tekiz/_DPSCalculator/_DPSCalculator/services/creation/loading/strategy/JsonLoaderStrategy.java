package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Loadable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jsonidmapper.JsonIDMapper;

public class JsonLoaderStrategy implements ObjectLoaderStrategy<JsonNode>
{
	private final ObjectMapper objectMapper;
	private final JsonIDMapper jsonIDMapper;

	public JsonLoaderStrategy(ObjectMapper objectMapper, JsonIDMapper jsonIDMapper)
	{
		this.objectMapper = objectMapper;
		this.jsonIDMapper = jsonIDMapper;
	}

	//need to be able to assign ID
	//todo - currently it won't use the ids assignable from the jsonIDMapper - need to fix this
	@Override
	public <T extends Loadable> T getData(String id, Class<T> classType, Optional<Factory<T, JsonNode>> factory) throws IOException
	{
		File jsonFile = jsonIDMapper.getFileFromID(id);
		if (jsonFile == null) return null;

		JsonNode rootNode = objectMapper.readTree(jsonFile);

		if (factory.isPresent()){
			return factory.get().createObject(rootNode);
		} else {
			return objectMapper.treeToValue(rootNode, classType);
		}
	}

	@Override
	public <T extends Loadable> List<T> getAllData(String type, Class<T> classType, Optional<Factory<T, JsonNode>> factory) throws IOException
	{
		List<T> resultList = new ArrayList<>();
		for (Map.Entry<String, File> entry : jsonIDMapper.getFileTypesFromData(type).entrySet()){
			JsonNode jsonNode = objectMapper.readTree(entry.getValue());
			if (factory.isPresent() && jsonNode != null){
				resultList.add(factory.get().createObject(jsonNode));
			} else if (jsonNode != null) {
				resultList.add(objectMapper.treeToValue(jsonNode, classType));
			}
		}
		return resultList;
	}
}

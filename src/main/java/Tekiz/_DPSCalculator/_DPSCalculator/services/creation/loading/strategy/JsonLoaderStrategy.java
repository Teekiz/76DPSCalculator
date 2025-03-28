package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsonidmapper.JsonIDMapper;

public class JsonLoaderStrategy implements ObjectLoaderStrategy
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
	public <T> T getData(String id, Class<T> classType, Factory<T> factory) throws IOException, ClassCastException
	{
		File jsonFile = jsonIDMapper.getFileFromID(id);
		if (jsonFile == null) return null;
		return loadObject(jsonFile, id, classType, factory);
	}

	@Override
	public <T> List<T> getAllData(String prefix, Class<T> classType, Factory<T> factory) throws IOException, ClassCastException
	{
		List<T> resultList = new ArrayList<>();
		for (Map.Entry<String, File> entry : jsonIDMapper.getFilesFromPrefix(prefix).entrySet()){
			JsonNode jsonNode = objectMapper.readTree(entry.getValue());
			((ObjectNode) jsonNode).put("id", entry.getKey());
			if (factory != null){
				resultList.add(factory.createObject(jsonNode));
			} else {
				resultList.add(objectMapper.treeToValue(jsonNode, classType));
			}
		}
		return resultList;
	}

	@Override
	public <T> T getDataByName(String objectName, Class<T> classType, Factory<T> factory) throws IOException
	{
		Map.Entry<String, File> entry = jsonIDMapper.getFileByName(objectName);
		if (entry.getValue() == null) return null;
		return loadObject(entry.getValue(), entry.getKey(), classType, factory);
	}

	private <T> T loadObject(File jsonFile, String id, Class<T> classType, Factory<T> factory) throws IOException
	{
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		((ObjectNode) rootNode).put("id", id);
		if (factory != null){
			return factory.createObject(rootNode);
		} else {
			return objectMapper.treeToValue(rootNode, classType);
		}
	}
}

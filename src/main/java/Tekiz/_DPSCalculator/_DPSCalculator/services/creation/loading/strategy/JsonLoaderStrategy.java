package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jsonidmapper.JsonIDMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonLoaderStrategy implements ObjectLoaderStrategy<JsonNode>
{
	private final ObjectMapper objectMapper;
	private final JsonIDMapper jsonIDMapper;

	@Autowired
	public JsonLoaderStrategy(ObjectMapper objectMapper, JsonIDMapper jsonIDMapper )
	{
		this.objectMapper = objectMapper;
		this.jsonIDMapper = jsonIDMapper;
	}

	//need to be able to assign ID
	//todo - currently it won't use the ids assignable from the jsonIDMapper - need to fix this
	@Override
	public <T> T getData(String id, Class<T> classType, Optional<Factory<T, JsonNode>> factory) throws IOException
	{
		File jsonFile = jsonIDMapper.getFileFromID(id);
		if (jsonFile == null) return null;

		JsonNode rootNode = objectMapper.readTree(jsonFile);
		if (factory.isPresent()){
			return factory.get().createObject(rootNode, id);
		} else {
			InjectableValues.Std injectableValues = new InjectableValues.Std().addValue("id", id);
			objectMapper.setInjectableValues(injectableValues);
			return objectMapper.treeToValue(rootNode, classType);
		}
	}

	@Override
	public <T> List<T> getAllData(String type, Class<T> classType, Optional<Factory<T, JsonNode>> factory) throws IOException
	{
		List<T> resultList = new ArrayList<>();
		for (Map.Entry<String, File> entry : jsonIDMapper.getFileTypesFromData(type).entrySet()){
			JsonNode jsonNode = objectMapper.readTree(entry.getValue());
			if (factory.isPresent() && jsonNode != null){
				resultList.add(factory.get().createObject(jsonNode, entry.getKey()));
			} else if (jsonNode != null) {
				InjectableValues.Std injectableValues = new InjectableValues.Std().addValue("id", entry.getKey());
				objectMapper.setInjectableValues(injectableValues);
				resultList.add(objectMapper.treeToValue(jsonNode, classType));
			}
		}
		return resultList;
	}
}

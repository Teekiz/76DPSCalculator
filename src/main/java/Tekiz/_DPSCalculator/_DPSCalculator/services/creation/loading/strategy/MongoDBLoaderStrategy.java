package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.persistence.MongoRepositoryInterface;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.ApplicationContext;

@Slf4j
public class MongoDBLoaderStrategy implements ObjectLoaderStrategy
{
	private final ApplicationContext applicationContext;

	public MongoDBLoaderStrategy(ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public <T> T getData(String id, Class<T> classType, Factory<T> factory)
	{
		MongoRepositoryInterface<T> repository = getRepository(classType);

		if (repository == null)
		{
			return null;
		}

		return repository.findById(id).orElse(null);
	}

	@Override
	public <T> T getDataByName(String objectName, Class<T> classType, Factory<T> factory)
	{
		MongoRepositoryInterface<T> repository = getRepository(classType);

		if (repository == null)
		{
			log.warn("Repository not found for class: {}.", classType.getName());
			return null;
		}

		return repository.findByName(objectName).orElse(null);
	}

	@Override
	public <T> List<T> getAllData(String type, Class<T> classType, Factory<T> factory)
	{
		MongoRepositoryInterface<T> repository = getRepository(classType);

		if (repository == null)
		{
			log.warn("Repository not found for class: {}.", classType.getName());
			return null;
		}

		return repository.findAll();

	}

	@SuppressWarnings("unchecked")
	private <T> MongoRepositoryInterface<T> getRepository(Class<T> classType)
	{
		RepositoryObject annotation = classType.getAnnotation(RepositoryObject.class);

		if (annotation == null) {
			log.warn("Repository not found for class: {}.", classType.getName());
			return null;
		}

		return (MongoRepositoryInterface<T>) applicationContext.getBean(annotation.repository());
	}
}

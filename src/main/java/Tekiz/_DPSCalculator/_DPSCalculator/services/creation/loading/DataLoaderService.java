package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.ObjectLoaderStrategy;
import java.io.IOException;
import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Setter
@Service
public class DataLoaderService {

	private ObjectLoaderStrategy objectLoaderStrategy;

	@Lazy
	@Autowired
	public DataLoaderService(ObjectLoaderStrategy objectLoaderStrategy) {
		this.objectLoaderStrategy = objectLoaderStrategy;
	}

	public <T, A> T loadData(String id, Class<T> classType, Factory<T, A> factory) throws IOException, IOException
	{
		return objectLoaderStrategy.getData(id, classType, factory);
	}

	public <T, A> List<T> loadAllData(String prefix, Class<T> classType, Factory<T, A> factory) throws IOException {
		return objectLoaderStrategy.getAllData(prefix, classType, factory);
	}
}

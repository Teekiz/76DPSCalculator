package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ObjectLoaderStrategy {
	 <T, A> T getData(String id, Class<T> classType, Factory<T, A> factory) throws IOException;
	<T, A> T getDataByName(String objectName, Class<T> classType, Factory<T, A> factory) throws IOException;
	 <T, A> List<T> getAllData(String type, Class<T> classType, Factory<T, A> factory) throws IOException;
}

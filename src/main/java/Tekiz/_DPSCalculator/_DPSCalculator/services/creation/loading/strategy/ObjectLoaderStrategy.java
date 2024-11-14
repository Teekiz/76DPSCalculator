package Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy;

import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Loadable;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.Factory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ObjectLoaderStrategy<A> {
	<T extends Loadable> T getData(String id, Class<T> classType, Optional<Factory<T, A>> factory) throws IOException;
	<T extends Loadable> List<T> getAllData(String type, Class<T> classType, Optional<Factory<T, A>> factory) throws IOException;
}

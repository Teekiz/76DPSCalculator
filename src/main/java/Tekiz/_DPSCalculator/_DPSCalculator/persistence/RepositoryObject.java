package Tekiz._DPSCalculator._DPSCalculator.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.data.repository.CrudRepository;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.strategy.MongoDBLoaderStrategy;

/**
 * An annotation used to help the {@link MongoDBLoaderStrategy} determine the correct {@link CrudRepository} to use.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RepositoryObject
{
	/**
	 * The repository for this object.
	 * @return {@link MongoRepositoryInterface}.
	 */
	Class<? extends MongoRepositoryInterface<?>> repository();
}

package Tekiz._DPSCalculator._DPSCalculator.persistence;

import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MongoRepositoryInterface<T> extends MongoRepository<T, String>
{
	Optional<T> findByName(String name);

	@Query(value = "{ '_id': ?0 }")
	Document findByIdAsDocument(String id);

	@Query(value = "{ 'name': ?0 }")
	Document findByNameAsDocument(String id);

	@Query(value = "{}")
	List<Document> findAllAsDocuments();
}

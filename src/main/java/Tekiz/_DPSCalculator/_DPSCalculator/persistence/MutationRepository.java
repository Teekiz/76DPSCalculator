package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import org.springframework.stereotype.Repository;

@Repository
public interface MutationRepository extends MongoRepositoryInterface<Mutation>
{
}

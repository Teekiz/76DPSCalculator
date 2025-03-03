package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmourRepository extends MongoRepositoryInterface<Armour>
{
}

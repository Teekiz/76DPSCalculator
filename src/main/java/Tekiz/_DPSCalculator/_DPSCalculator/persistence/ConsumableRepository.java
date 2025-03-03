package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumableRepository extends MongoRepositoryInterface<Consumable>
{}

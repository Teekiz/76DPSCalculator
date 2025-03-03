package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import org.springframework.stereotype.Repository;

@Repository
public interface PerkRepository extends MongoRepositoryInterface<Perk>
{
}


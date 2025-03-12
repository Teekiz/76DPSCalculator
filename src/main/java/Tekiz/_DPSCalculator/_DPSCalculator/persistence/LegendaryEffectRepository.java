package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import org.springframework.stereotype.Repository;

@Repository
public interface LegendaryEffectRepository extends MongoRepositoryInterface<LegendaryEffect>
{
}

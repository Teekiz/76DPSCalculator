package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.enemy.Enemy;
import org.springframework.stereotype.Repository;

@Repository
public interface EnemyRepository extends MongoRepositoryInterface<Enemy>
{
}

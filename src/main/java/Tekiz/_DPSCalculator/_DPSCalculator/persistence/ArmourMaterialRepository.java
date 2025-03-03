package Tekiz._DPSCalculator._DPSCalculator.persistence;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmourMaterialRepository extends MongoRepositoryInterface<Material>
{
}

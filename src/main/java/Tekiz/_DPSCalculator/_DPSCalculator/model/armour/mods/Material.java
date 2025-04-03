package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourMaterialRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a material modification that can be made to a piece of armour.
 * Each {@link Material} contains the bonus resistances and effect the mod may have.
 */

@Value
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "material")
@RepositoryObject(repository = ArmourMaterialRepository.class)
public class Material extends ArmourMod
{
	@JsonProperty("armourResistance")
	HashMap<Integer, ArmourResistance> materialResistanceBonus;
	@JsonProperty("materialEffect")
	String materialEffect;
}

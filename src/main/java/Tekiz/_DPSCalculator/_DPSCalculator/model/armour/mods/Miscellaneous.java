package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourMiscellaniousRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a miscellaneous modification that can be made to a piece of armour.
 * Each {@link Material} effect the mod may have.
 */
@Value
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document(collection = "miscellaneous")
@RepositoryObject(repository = ArmourMiscellaniousRepository.class)
public class Miscellaneous extends ArmourMod
{
	@JsonProperty("modEffect")
	String modEffect;
}

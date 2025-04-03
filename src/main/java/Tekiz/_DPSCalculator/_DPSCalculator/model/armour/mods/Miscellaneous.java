package Tekiz._DPSCalculator._DPSCalculator.model.armour.mods;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.properties.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourPiece;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourMiscellaniousRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a miscellaneous modification that can be made to a piece of armour.
 * Each {@link Material} effect the mod may have.
 */
@Document(collection = "miscellaneous")
@RepositoryObject(repository = ArmourMiscellaniousRepository.class)
public record Miscellaneous (@Id
							 @JsonProperty("id") @JsonAlias("_id") String id,
							 @JsonProperty("name") String name,
							 @JsonProperty("armourPiece") ArmourPiece armourPiece,
							 @JsonProperty("modEffect") String modEffect) implements ArmourMod
{}

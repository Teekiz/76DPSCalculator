package Tekiz._DPSCalculator._DPSCalculator.model.enemy;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourResistance;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import Tekiz._DPSCalculator._DPSCalculator.persistence.EnemyRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Represents the enemy or target. */
@Getter
@AllArgsConstructor
@Document(collection = "enemy")
@RepositoryObject(repository = EnemyRepository.class)
public class Enemy
{
	@Id
	@JsonProperty("id") @JsonAlias("_id")
	private final String id;

	@JsonProperty("name")
	private final String name;

	@JsonProperty("isGlowing")
	private final boolean isGlowing;

	@JsonProperty("enemyType")
	private final EnemyType enemyType;

	@JsonProperty("armourResistance")
	private final ArmourResistance armourResistance;
}

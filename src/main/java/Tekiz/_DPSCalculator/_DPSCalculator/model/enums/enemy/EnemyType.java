package Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy;

import java.util.Map;
import java.util.Set;
import lombok.Getter;

@Getter
public enum EnemyType
{
	HUMAN(Set.of(EnemyTags.HUMAN), Map.of(
		Limbs.HEAD, 2.0,
		Limbs.TORSO, 1.0,
		Limbs.LEFT_ARM, 1.0,
		Limbs.RIGHT_ARM, 1.0,
		Limbs.LEFT_LEG, 1.0,
		Limbs.RIGHT_LEG, 1.0
	)),
	GHOUL(Set.of(EnemyTags.GHOUL), Map.of(
		Limbs.HEAD, 2.0,
		Limbs.TORSO, 1.0,
		Limbs.LEFT_ARM, 1.0,
		Limbs.RIGHT_ARM, 1.0,
		Limbs.LEFT_LEG, 1.0,
		Limbs.RIGHT_LEG, 1.0
	)),
	SUPER_MUTANT(Set.of(EnemyTags.SUPER_MUTANT), Map.of(
		Limbs.HEAD, 2.0,
		Limbs.TORSO, 1.0,
		Limbs.LEFT_ARM, 1.0,
		Limbs.RIGHT_ARM, 1.0,
		Limbs.LEFT_LEG, 1.0,
		Limbs.RIGHT_LEG, 1.0
	)),
	MR_HANDY(Set.of(EnemyTags.ROBOT), Map.of(
		Limbs.LEFT_EYE, 1.25,
		Limbs.MIDDLE_EYE, 1.25,
		Limbs.RIGHT_EYE, 1.25,
		Limbs.TORSO, 0.6,
		Limbs.THRUSTER, 1.0,
		Limbs.LEFT_LEG, 1.0,
		Limbs.MIDDLE_LEG, 1.0,
		Limbs.RIGHT_LEG, 1.0
	)),;

	private final Set<EnemyTags> tags;
	private final Map<Limbs, Double> limbs;

	EnemyType(Set<EnemyTags> tags, Map<Limbs, Double> limbs)
	{
		this.tags = tags;
		this.limbs = limbs;
	}
}

package Tekiz._DPSCalculator._DPSCalculator.model.enemy;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.enemy.EnemyType;
import lombok.Getter;
import lombok.Setter;

/** Represents the enemy or target. */
@Getter
public class Enemy
{
	@Setter
	private boolean isGlowing = false;
	private final EnemyType enemyType;


	public Enemy(EnemyType enemyType)
	{
		this.enemyType = enemyType;
	}
}

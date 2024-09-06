package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SneakAndCriticalModifier
{
	private double sneakBonus;
	private double criticalBonus;

	public void addSneakBonus(double addedBonus)
	{
		sneakBonus+=addedBonus;
	}

	public void addCriticalBonus(double addedBonus)
	{
		criticalBonus+=addedBonus;
	}
}

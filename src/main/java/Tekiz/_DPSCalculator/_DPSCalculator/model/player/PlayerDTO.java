package Tekiz._DPSCalculator._DPSCalculator.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO
{
	private double maxHP;
	private double currentHP;
	private SpecialDTO specials;
}

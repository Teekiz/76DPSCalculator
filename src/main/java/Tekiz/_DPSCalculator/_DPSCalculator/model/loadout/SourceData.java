package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.BonusTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SourceData
{
	private BonusTypes bonusTypes;
	private Double value;
}

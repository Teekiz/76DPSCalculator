package Tekiz._DPSCalculator._DPSCalculator.model.character.perk;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.enums.BonusType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Perk
{
	private String perkName;
	private PerkTypes perkType;
	private BonusType bonusType;
	private String perkDescription;
	private String condition;
	private List<PerkRankEffects> perkRankEffects;
}

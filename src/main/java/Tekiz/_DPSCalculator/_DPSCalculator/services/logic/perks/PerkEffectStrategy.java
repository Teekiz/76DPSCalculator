package Tekiz._DPSCalculator._DPSCalculator.services.logic.perks;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;

public interface PerkEffectStrategy
{
	Object applyEffect(Perk perk, int perkRank);
}

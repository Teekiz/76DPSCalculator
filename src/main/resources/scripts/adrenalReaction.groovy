package scripts

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout
import Tekiz._DPSCalculator._DPSCalculator.util.map.MapUtil;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;

class adrenalReaction extends Script
{
    def run(Loadout loadout) {
        double hpPercentage = loadout.player.healthPercentage
        hpPercentage = Math.min(hpPercentage, 100.0)
        def adrenalReactionMap = [
                (0.0) : 0.50,
                (20.0) : 0.50,
                (30.0) : 0.44,
                (40.0) : 0.38,
                (50.0) : 0.31,
                (60.0) : 0.25,
                (70.0) : 0.19,
                (80.0) : 0.13,
                (90.0) : 0.06,
                (100.0) : 0.0
        ]

        // find the lowest value corresponding to the hpPercentageProvided
        def value = adrenalReactionMap.find
        {
            it.key >= hpPercentage
        }?.value

        return MapUtil.createEntry(ModifierTypes.DAMAGE_ADDITIVE, value.doubleValue())
    }

    @Override
    Object run() {
        return null
    }
}

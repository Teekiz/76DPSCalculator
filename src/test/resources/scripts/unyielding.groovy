package scripts

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout
import Tekiz._DPSCalculator._DPSCalculator.util.map.MapUtil;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.ModifierTypes;

class testUnyielding extends Script
{
    def run(Loadout loadout) {
        double hpPercentage = loadout.player.healthPercentage
        hpPercentage = Math.min(hpPercentage, 100.0)
        def specialBonusMap = [
                (0.0) : 3,
                (19.99) : 3,
                (39.99) : 2,
                (59.99) : 1,
                (100.0) : 0.0
        ]

        // find the lowest value corresponding to the hpPercentageProvided
        def value = specialBonusMap.find {
            it.key >= hpPercentage
        }?.value

        def unyieldingMap = [:]
        List<Map.Entry> mapEntries = [MapUtil.createEntry(ModifierTypes.SPECIAL_STRENGTH, value.intValue()),
                                      MapUtil.createEntry(ModifierTypes.SPECIAL_PERCEPTION, value.intValue()),
                                      MapUtil.createEntry(ModifierTypes.SPECIAL_CHARISMA, value.intValue()),
                                      MapUtil.createEntry(ModifierTypes.SPECIAL_INTELLIGENCE, value.intValue()),
                                      MapUtil.createEntry(ModifierTypes.SPECIAL_AGILITY, value.intValue()),
                                      MapUtil.createEntry(ModifierTypes.SPECIAL_LUCK, value.intValue())]
        mapEntries.each { entry ->
            unyieldingMap[entry.key] = entry.value
        }

        return unyieldingMap
    }

    @Override
    Object run() {
        return null
    }
}

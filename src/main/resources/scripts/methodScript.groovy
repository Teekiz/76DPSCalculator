package scripts

class methodScript extends Script
{
    def getAdrenalReactionValue(double hpPercentage) {
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
        return value.doubleValue()
    }

    @Override
    Object run() {
        return null
    }
}

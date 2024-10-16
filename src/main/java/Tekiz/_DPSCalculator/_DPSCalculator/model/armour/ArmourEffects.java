package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Value;

/** Represents the effects a given armour piece provides. */
//todo - write set effects for some armour types
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ArmourEffects implements Serializable
{

}

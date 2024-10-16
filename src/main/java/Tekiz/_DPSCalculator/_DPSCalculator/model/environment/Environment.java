package Tekiz._DPSCalculator._DPSCalculator.model.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Value;

/** Represents an environment and its details such as time of day. */
//todo - this includes the time of day, possibly distance to enemy.
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Environment implements Serializable
{

}

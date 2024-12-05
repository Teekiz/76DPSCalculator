package Tekiz._DPSCalculator._DPSCalculator.model.consumables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A data transfer object for an object representing {@link Consumable}.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumableDTO
{
	private String id;
	private String name;
	private String consumableType;
	private String addictionType;

}

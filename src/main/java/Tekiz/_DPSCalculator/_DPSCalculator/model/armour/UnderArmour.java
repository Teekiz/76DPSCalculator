package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
public class UnderArmour extends Armour
{
	@Override
	public void setMod(ArmourMod armourMod)
	{}

	@Override
	public List<Modifier> getAllModificationEffects()
	{
		return List.of();
	}
}

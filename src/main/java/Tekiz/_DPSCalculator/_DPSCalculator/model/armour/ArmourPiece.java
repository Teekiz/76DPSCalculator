package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
public class ArmourPiece extends Armour
{
	//mods
	/** The {@link Material} mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	@NonFinal
	@JsonProperty("material")
	Material armourMaterial;
	/** The {@link Miscellaneous} mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	@NonFinal
	@JsonProperty("miscellaneous")
	Miscellaneous armourMisc;

	//todo - add check to ensure piece matches
	/**
	 * A method that is used to make modifications to the armour.
	 * @param armourMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ArmourMod} object.
	 */
	@JsonIgnore
	public void setMod(ArmourMod armourMod)
	{
		switch (armourMod)
		{
			case Material material -> armourMaterial = material;
			case Miscellaneous miscellaneous -> armourMisc = miscellaneous;
			default -> {}
		}
	}
}

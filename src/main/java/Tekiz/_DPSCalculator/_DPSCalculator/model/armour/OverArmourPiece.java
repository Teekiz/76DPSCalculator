package Tekiz._DPSCalculator._DPSCalculator.model.armour;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.armour.ArmourClassification;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@EqualsAndHashCode(callSuper = true)
@Jacksonized
@SuperBuilder(toBuilder = true)
public class OverArmourPiece extends Armour
{
	@JsonProperty("armourClassification")
	ArmourClassification armourClassification;

	/** The material mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	@JsonProperty("material")
	ModificationSlot<ArmourMod> material;

	/** The miscellaneous mod slot of the armour piece. This affects the resistances and effects the armour provides. */
	@JsonProperty("miscellaneous")
	ModificationSlot<ArmourMod> miscellaneous;

	//todo - add check to ensure piece matches
	/**
	 * A method that is used to make modifications to the armour.
	 * @param armourMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ArmourMod} object.
	 */
	@JsonIgnore
	public void setMod(ArmourMod armourMod)
	{
		if (armourMod == null){
			return;
		}

		ModificationSlot<ArmourMod> slot = null;

		switch (armourMod.modType())
		{
			case MATERIAL -> slot = material;
			case MISCELLANEOUS -> slot = miscellaneous;
			default -> {}
		}

		if (slot != null){
			slot.changeCurrentModification(armourMod);
		}
	}

	@Override
	public List<Modifier> getAllModificationEffects()
	{
		List<Optional<ModificationSlot<?>>> modifications = List.of(Optional.ofNullable(material), Optional.ofNullable(miscellaneous));
		return modifications.stream()
			.flatMap(Optional::stream)
			.map(ModificationSlot::getCurrentModification)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}
}

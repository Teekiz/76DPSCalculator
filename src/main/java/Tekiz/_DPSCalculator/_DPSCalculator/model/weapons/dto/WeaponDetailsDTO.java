package Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * A data transfer object for an object representing weapons (limited data only).
 */
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class WeaponDetailsDTO
{
	private String id;
	private String name;
	private String weaponType;
	private HashMap<Integer, List<WeaponDamage>> weaponDamageByLevel;
	private int apCost;
	private int criticalBonus;
	private HashMap<ModType, ModificationSlot<WeaponMod>> modifications;
}

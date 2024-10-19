package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponNameDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link Weapon} object to a {@link WeaponNameDTO} object.
 */
@Service
public class WeaponMapper
{
	/**
	 * A method to convert a single weapon into a data transfer object (DTO).
	 * @param weapon The {@link Weapon} to be converted.
	 * @return The {@link Weapon} represented as a DTO ({@link WeaponNameDTO}) or null if the perk is null.
	 */
	public WeaponNameDTO convertToNameDTO(Weapon weapon)
	{
		if (weapon == null) {return null;}
		return new WeaponNameDTO(weapon.getWeaponID(), weapon.getWeaponName());
	}

	/**
	 * A method to convert all weapons in a list to a data transfer object (DTO) representation.
	 * @param weapons A {@link List} of {@link Weapon} to be converted.
	 * @return A {@link List} of {@link Weapon} represented as a DTOs ({@link WeaponNameDTO}).
	 */
	public List<WeaponNameDTO> convertAllToNameDTO(List<Weapon> weapons)
	{
		return weapons.stream()
			.map(this::convertToNameDTO)
			.collect(Collectors.toList());
	}
}

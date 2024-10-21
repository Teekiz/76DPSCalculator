package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.MeleeWeaponDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.RangedWeaponDTO;
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
	 * @return The {@link Weapon} represented as a DTO ({@link WeaponNameDTO}) or null if the weapon is null.
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

	/**
	 * A method to convert a single weapon into a data transfer object (DTO).
	 * @param weapon The {@link Weapon} to be converted.
	 * @return The {@link Weapon} represented as a DTO ({@link WeaponDetailsDTO}) or null if the weapon is null.
	 */
	public WeaponDetailsDTO convertToDetailsDTO(Weapon weapon)
	{
		if (weapon==null){return null;}
		return WeaponDetailsDTO.builder()
			.weaponID(weapon.getWeaponID())
			.weaponName(weapon.getWeaponName())
			.weaponType(weapon.getWeaponType().toString())
			.damageType(weapon.getDamageType().toString())
			.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
			.apCost(weapon.getApCost())
			.build();
	}

	/**
	 * A method to convert a single weapon into a data transfer object (DTO).
	 * @param weapon The {@link Weapon} to be converted.
	 * @return The {@link Weapon} represented as a DTO ({@link RangedWeaponDTO}) or null if the weapon is null.
	 */
	public WeaponDetailsDTO convertToRangedOrMeleeDTO(Weapon weapon)
	{
		if (weapon instanceof RangedWeapon) {
			return RangedWeaponDTO.builder()
				.weaponID(weapon.getWeaponID())
				.weaponName(weapon.getWeaponName())
				.weaponType(weapon.getWeaponType().toString())
				.damageType(weapon.getDamageType().toString())
				.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
				.apCost(weapon.getApCost())
				.magazineSize(((RangedWeapon) weapon).getMagazineSize())
				.fireRate(((RangedWeapon) weapon).getFireRate())
				.range(((RangedWeapon) weapon).getRange())
				.accuracy(((RangedWeapon) weapon).getAccuracy())
				.build();
		} else if (weapon instanceof MeleeWeapon) {
			return MeleeWeaponDTO.builder()
				.weaponID(weapon.getWeaponID())
				.weaponName(weapon.getWeaponName())
				.weaponType(weapon.getWeaponType().toString())
				.damageType(weapon.getDamageType().toString())
				.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
				.apCost(weapon.getApCost())
				.build();
		} else {
			return convertToDetailsDTO(weapon);
		}
	}
}

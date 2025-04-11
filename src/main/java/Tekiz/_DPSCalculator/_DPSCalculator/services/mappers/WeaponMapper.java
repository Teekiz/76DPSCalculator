package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.weapons.MeleeWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.MeleeWeaponDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponModDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponModNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponNameDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.RangedWeaponDTO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link Weapon} object to a {@link WeaponNameDTO} object.
 */
@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class WeaponMapper
{
	private final ModifierMapper modifierMapper;

	/**
	 * A method to convert a single weapon into a data transfer object (DTO).
	 * @param weapon The {@link Weapon} to be converted.
	 * @return The {@link Weapon} represented as a DTO ({@link WeaponNameDTO}) or null if the weapon is null.
	 */
	public WeaponNameDTO convertToNameDTO(Weapon weapon)
	{
		if (weapon == null) {return null;}
		return new WeaponNameDTO(weapon.getId(), weapon.getName());
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
			.id(weapon.getId())
			.name(weapon.getName())
			.weaponType(weapon.getWeaponType().toString())
			.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
			.apCost(weapon.getApCost())
			.modifications(weapon.getModifications())
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
				.id(weapon.getId())
				.name(weapon.getName())
				.weaponType(weapon.getWeaponType().toString())
				.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
				.apCost(weapon.getApCost())
				.magazineSize(((RangedWeapon) weapon).getMagazineSize())
				.fireRate(((RangedWeapon) weapon).getFireRate())
				.range(((RangedWeapon) weapon).getRange())
				.accuracy(((RangedWeapon) weapon).getAccuracy())
				.modifications(weapon.getModifications())
				.build();
		} else if (weapon instanceof MeleeWeapon) {
			return MeleeWeaponDTO.builder()
				.id(weapon.getId())
				.name(weapon.getName())
				.weaponType(weapon.getWeaponType().toString())
				.weaponDamageByLevel(weapon.getWeaponDamageByLevel())
				.apCost(weapon.getApCost())
				.attackSpeed(((MeleeWeapon) weapon).getAttackSpeed().toString())
				.modifications(weapon.getModifications())
				.build();
		} else {
			return convertToDetailsDTO(weapon);
		}
	}

	/**
	 * A method to convert all weapon mod provided into a data transfer object (DTO).
	 * @param mods The {@link WeaponMod}s to be converted.
	 * @return A list of {@link WeaponMod}s represented as a DTO ({@link WeaponModDTO}).
	 */
	public List<WeaponModNameDTO> convertToWeaponModNameDTO(List<WeaponMod> mods){
		if (mods == null){return Collections.emptyList();}
		return mods.stream()
			.map(entry -> WeaponModNameDTO.builder()
				.id(entry.id())
				.name(entry.name())
				.build())
			.collect(Collectors.toList());
	}

	/**
	 * A method to convert a single weapon mod into a data transfer object (DTO).
	 * @param weaponMod The {@link WeaponMod} to be converted.
	 * @return The {@link WeaponMod} represented as a DTO ({@link WeaponModDTO}).
	 */
	public WeaponModDTO convertToWeaponModDTO(WeaponMod weaponMod){
		if (weaponMod==null){return null;}
		return WeaponModDTO.builder()
			.id(weaponMod.id())
			.name(weaponMod.name())
			.modType(weaponMod.modType())
			.modificationEffects(modifierMapper.convertAllModifiersToDTO(weaponMod.effects()))
			.build();
	}
}

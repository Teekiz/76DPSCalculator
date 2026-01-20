package Tekiz._DPSCalculator._DPSCalculator.model.weapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.Category;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Keyable;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffect;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.weapons.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectCompatible;
import Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects.LegendaryEffectSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.damage.WeaponDamage;
import Tekiz._DPSCalculator._DPSCalculator.persistence.RepositoryObject;
import Tekiz._DPSCalculator._DPSCalculator.persistence.WeaponRepository;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.stream.Collectors;
import lombok.Getter;
import Tekiz._DPSCalculator._DPSCalculator.model.interfaces.Modifier;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a generic weapon that the user can add to their loadout.
 */
@Slf4j
@Getter
@SuperBuilder(toBuilder = true)
@Document(collection = "weapon")
@RepositoryObject(repository = WeaponRepository.class)
public abstract class Weapon implements Serializable, LegendaryEffectCompatible
{
	//todo - consider changing to add armour penetration and removing projectile amount
	/** The id of the weapon. Used if mapped to a database. */
	@Id
	@JsonProperty("id") @JsonAlias("_id")
	protected final String id;

	/** The name of the weapon. The user will be able to see the given value. */
	@JsonProperty("name")
	protected final String name;

	/** The type of weapon. This is used for various {@link Modifier} conditions. */
	@JsonProperty("weaponType")
	protected final WeaponType weaponType;

	/** The level the weapon is set to. */
	@JsonProperty("weaponLevel")
	protected int weaponLevel;

	/** A {@link HashMap} of the weapons level ({@link Integer}) and the base damage it provides ({@link Double}). */
	@JsonProperty("weaponDamageByLevel")
	protected final HashMap<Integer, List<WeaponDamage>> weaponDamageByLevel;

	/** The {@link Integer} value that it costs to use the weapon. */
	@JsonProperty("apCost")
	protected final int apCost;

	/** The {@link Double} value of the bonus to critical damage the weapon provides. Primarily used for VATS. */
	@JsonProperty("criticalBonus")
	protected final double criticalBonus;

	//todo - provide default legendary effect slots (i.e. provide 4 star effects by default and other slots if required (such as cursed _1STAR))
	/** An object containing legendary effects in a HashMap*/
	@JsonProperty("legendaryEffects")
	protected HashMap<StarType, LegendaryEffectSlot> legendaryEffects;

	/** An object to simplify modification lookups. */
	@JsonProperty("modifications")
	protected HashMap<ModType, ModificationSlot<WeaponMod>> modifications;

	/**
	 * Retrieves the {@link Double} value of the damage the weapon provides.
	 * @param weaponLevel The level used to retrieve the provided damage value by corresponding {@code weaponDamageByLevel} key.
	 * @return The {@code weaponDamageByLevel} corresponding damage value.
	 */
	@JsonIgnore
	public List<WeaponDamage> getBaseDamage(int weaponLevel)
	{
		return weaponDamageByLevel.get(weaponLevel);
	}

	/**
	 * A method to check if a weapons damage contains a certain damage type.
	 * @param damageType The type of damage to check for.
	 * @param exclusiveToWeaponType If {@code true}, then this will only return true if the damage type matches and no other damage types exist.
	 * @return {@code true} if weapon uses a matching damage type.
	 */
	@JsonIgnore
	public boolean containsDamageType(DamageType damageType, boolean exclusiveToWeaponType)
	{
		List<WeaponDamage> damageTypeList = weaponDamageByLevel.values().stream().findFirst().orElse(new ArrayList<>());
		for (WeaponDamage weaponDamage : damageTypeList){
			if (weaponDamage.damageType().equals(damageType)){
				return !exclusiveToWeaponType || damageTypeList.size() == 1;
			}
		}

		return false;
	}

	/**
	 * A method that is used to make modifications to the weapon.
	 * @param weaponMod The modification that the user wishes to make. The mod slot it affects is determined by the class of the {@link ModType}.
	 * @return {@code true} if the modification was applied.
	 */
	@JsonIgnore
	public boolean setMod(WeaponMod weaponMod)
	{
		if (weaponMod == null){
			return false;
		}

		ModificationSlot<WeaponMod> slot = modifications.get(weaponMod.modType());
		if (slot != null){
			return slot.changeCurrentModification(weaponMod);
		}
		return false;
	}

	/**
	 * A method to update the current legendary effect
	 * @param starType The slot the effect will be applied to.
	 * @param legendaryEffect The new legendary effect (will remove the effect null)
	 */
	@JsonIgnore
	public void modifyLegendaryEffect(StarType starType, LegendaryEffect legendaryEffect){
		Category category = Category.getClassCategory(this.getClass());
		LegendaryEffectSlot slot = legendaryEffects.get(starType);

		//if the slot cannot be found.
		if (slot == null){
			return;
		}

		//todo - remove lower tier slots.
		//if the slot can be found, update the slot (null object effectively removes the effect)
		if (legendaryEffect == null || legendaryEffect.categories().contains(category)) {
			slot.changeCurrentLegendaryEffect(legendaryEffect);
		}
	}

	/**
	 * A method that gets the effects from the modifications and legendary effects.
	 * @return A {@link List} of {@link Modifier}'s and {@link LegendaryEffect}'s.
	 */
	@JsonIgnore
	public List<Modifier> getAllModificationEffects()
	{
		List<Modifier> modifiers = new ArrayList<>(legendaryEffects != null ? legendaryEffects.values().stream().map(LegendaryEffectSlot::getCurrentLegendaryEffect).toList() : List.of());

		if (modifications != null){
			modifiers.addAll(modifications.values().stream()
				.map(ModificationSlot::getCurrentModification)
				.filter(Objects::nonNull)
				.toList());
		}

		return modifiers;
	}

	/**
	 * A method to set a weapons level.
	 *
	 * @param targetWeaponLevel The target weapon level. If the weapon does not contain the set level, it will set it to the closest available level rounded up as defined in {@code weaponDamageByLevel}.
	 */
	@JsonIgnore
	public void setWeaponLevel(int targetWeaponLevel){
		Map<Integer, List<WeaponDamage>> damageByLevel = getWeaponDamageByLevel();
		int closestValidLevel = 1;

		if (damageByLevel == null || damageByLevel.isEmpty()){
			log.error("Weapon {} has null levels. Defaulting to level 1", id);
			weaponLevel = closestValidLevel;
			return;
		}

		List<Integer> sortedLevels = damageByLevel.keySet().stream()
			.sorted()
			.toList();

		int selectedLevel = sortedLevels.getLast();

		for (int level : sortedLevels) {
			if (level >= targetWeaponLevel) {
				selectedLevel = level;
				break;
			}
		}

		weaponLevel = selectedLevel;
	}
}

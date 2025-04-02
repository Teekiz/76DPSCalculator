package Tekiz._DPSCalculator._DPSCalculator.model.player;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents a players SPECIAL stats. */
@Getter
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Special implements Serializable
{
	@JsonProperty("strength")
	private int strength;
	@JsonProperty("perception")
	private int perception;
	@JsonProperty("endurance")
	private int endurance;
	@JsonProperty("charisma")
	private int charisma;
	@JsonProperty("intelligence")
	private int intelligence;
	@JsonProperty("agility")
	private int agility;
	@JsonProperty("luck")
	private int luck;

	@JsonProperty("strengthBoost")
	private int strengthBoost;
	@JsonProperty("perceptionBoost")
	private int perceptionBoost;
	@JsonProperty("enduranceBoost")
	private int enduranceBoost;
	@JsonProperty("charismaBoost")
	private int charismaBoost;
	@JsonProperty("intelligenceBoost")
	private int intelligenceBoost;
	@JsonProperty("agilityBoost")
	private int agilityBoost;
	@JsonProperty("luckBoost")
	private int luckBoost;

	@JsonProperty("minSpecialValue")
	private final int minSpecialValue = 1;
	@JsonProperty("maxSpecialValue")
	private final int maxSpecialValue = 15;
	@JsonProperty("maxSpecialWithBoostValue")
	private final int maxSpecialWithBoostValue = 100;

	/**
	 * A method to modify the players special stats.
	 * @param special The stat the user wishes to modify. See {@link Specials} for valid values.
	 * @param value The {@link Integer} value the user wishes to set to the {@code special} to. The target stat cannot be set below 1 or higher than 15.
	 */
	public void setSpecial(Specials special, int value)
	{
		switch (special)
		{
			case STRENGTH -> strength = setSpecialCheck(value);
			case PERCEPTION -> perception = setSpecialCheck(value);
			case ENDURANCE -> endurance = setSpecialCheck(value);
			case CHARISMA -> charisma = setSpecialCheck(value);
			case INTELLIGENCE -> intelligence = setSpecialCheck(value);
			case AGILITY -> agility = setSpecialCheck(value);
			case LUCK -> luck = setSpecialCheck(value);
		}
	}

	/**
	 * A method to modify the boosts the player has currently from various modifiers.
	 * @param special The stat the user wishes to modify. See {@link Specials} for valid values.
	 * @param value The {@link Integer} value the user wishes to set the {@code special} to. The target stat cannot be set below 1 or higher than 15.
	 */
	public void setSpecialBoost(Specials special, int value)
	{
		switch (special)
		{
			case STRENGTH -> strengthBoost = value;
			case PERCEPTION -> perceptionBoost = value;
			case ENDURANCE -> enduranceBoost = value;
			case CHARISMA -> charismaBoost = value;
			case INTELLIGENCE -> intelligenceBoost = value;
			case AGILITY -> agilityBoost = value;
			case LUCK -> luckBoost = value;
		}
	}

	/**
	 * A method that the special class uses internally to ensure that a SPECIALs value does not go above 1 or below 15.
	 * @param value The {@link Integer} value that the user wishes to add or subtract from the @{code current} value.
	 * @return {@link Integer} The modified value between the defined range (1 - 15).
	 */
	private int setSpecialCheck(int value)
	{
		return Math.min(Math.max(value, minSpecialValue), maxSpecialValue);
	}

	/**
	 * A method to change all specials in one go. Each value is checked against the minimum and maximum values before being applied.
	 * @param strength The new {@code strength} value.
	 * @param perception The new {@code perception} value.
	 * @param endurance The new {@code endurance} value.
	 * @param charisma The new {@code charisma} value.
	 * @param intelligence The new {@code intelligence} value.
	 * @param agility The new {@code agility} value.
	 * @param luck The new {@code luck} value.
	 */
	public void setAllSpecials(int strength, int perception, int endurance, int charisma, int intelligence, int agility, int luck)
	{
		//checks each special before applying them
		this.strength = setSpecialCheck(strength);
		this.perception = setSpecialCheck(perception);
		this.endurance = setSpecialCheck(endurance);
		this.charisma = setSpecialCheck(charisma);
		this.intelligence = setSpecialCheck(intelligence);
		this.agility = setSpecialCheck(agility);
		this.luck = setSpecialCheck(luck);
	}

	/**
	 * A method to change all specials boosts.
	 * @param strength The new {@code strengthBoost} value.
	 * @param perception The new {@code perceptionBoost} value.
	 * @param endurance The new {@code enduranceBoost} value.
	 * @param charisma The new {@code charismaBoost} value.
	 * @param intelligence The new {@code intelligenceBoost} value.
	 * @param agility The new {@code agilityBoost} value.
	 * @param luck The new {@code luckBoost} value.
	 */
	public void setAllSpecialsBoosts(int strength, int perception, int endurance, int charisma, int intelligence, int agility, int luck)
	{
		//checks each special before applying them
		this.strengthBoost = strength;
		this.perceptionBoost = perception;
		this.enduranceBoost = endurance;
		this.charismaBoost = charisma;
		this.intelligenceBoost = intelligence;
		this.agilityBoost = agility;
		this.luckBoost = luck;
	}

	/**
	 * A method used to get the corresponding special value.
	 *
	 * @param special      The special value to retrieve.
	 * @param includeBoost If the boosted value should be included
	 * @return A {@link Integer} value of the corresponding {@code special} value.
	 */
	public int getSpecialValue(Specials special, boolean includeBoost){
		int value = 0;
		switch (special)
		{
			case STRENGTH -> value = includeBoost ? strength + strengthBoost : strength;
			case PERCEPTION -> value = includeBoost ? perception + perceptionBoost : perception;
			case ENDURANCE -> value = includeBoost ? endurance + enduranceBoost : endurance;
			case CHARISMA -> value = includeBoost ? charisma + charismaBoost : charisma;
			case INTELLIGENCE -> value = includeBoost ? intelligence + intelligenceBoost : intelligence;
			case AGILITY -> value = includeBoost ? agility + agilityBoost : agility;
			case LUCK -> value = includeBoost ? luck + luckBoost : luck;
		}
		return Math.max(includeBoost ? Math.min(value, maxSpecialWithBoostValue) : Math.min(value, maxSpecialValue), minSpecialValue);
	}
}

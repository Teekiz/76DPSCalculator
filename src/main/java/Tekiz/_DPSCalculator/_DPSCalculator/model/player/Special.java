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

	@JsonProperty("minSpecialValue")
	private final int minSpecialValue = 1;
	@JsonProperty("maxSpecialValue")
	private final int maxSpecialValue = 15;

	/**
	 * A method to modify the players special stats.
	 * @param special The stat the user wishes to modify. See {@link Specials} for valid values.
	 * @param value The {@link Integer} value the user wishes to add or subtract to the {@code special}. The target stat cannot be set below 1 or higher than 15.
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
	 * A method used to get the corresponding special value.
	 * @param special The special value to retrieve.
	 * @return A {@link Integer} value of the corresponding {@code special} value.
	 */
	public int getSpecialValue(Specials special){
		int value = 1;
		switch (special)
		{
			case STRENGTH -> value = strength;
			case PERCEPTION -> value = perception;
			case ENDURANCE -> value = endurance;
			case CHARISMA -> value = charisma;
			case INTELLIGENCE -> value = intelligence;
			case AGILITY -> value = agility;
			case LUCK -> value = luck;
		}
		return value;
	}
}

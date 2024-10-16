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
	public void modifySpecial(Specials special, int value)
	{
		switch (special)
		{
			case STRENGTH -> strength = modifySpecialCheck(strength, value);
			case PERCEPTION -> perception = modifySpecialCheck(perception, value);
			case ENDURANCE -> endurance = modifySpecialCheck(endurance, value);
			case CHARISMA -> charisma = modifySpecialCheck(charisma, value);
			case INTELLIGENCE -> intelligence = modifySpecialCheck(intelligence, value);
			case AGILITY -> agility = modifySpecialCheck(agility, value);
			case LUCK -> luck = modifySpecialCheck(luck, value);
		}
	}

	/**
	 * A method that the special class uses internally to ensure that a SPECIALs value does not go above 1 or below 15.
	 * @param current The {@link Integer} value of the chosen special stat.
	 * @param value The {@link Integer} value that the user wishes to add or subtract from the @{code current} value.
	 * @return {@link Integer} The modified value between the defined range (1 - 15).
	 */
	private int modifySpecialCheck(int current, int value)
	{
		if (current + value > maxSpecialValue) return maxSpecialValue;
		else return Math.max(current + value, minSpecialValue);
	}
}

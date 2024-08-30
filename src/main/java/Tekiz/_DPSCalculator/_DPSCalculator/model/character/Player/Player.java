package Tekiz._DPSCalculator._DPSCalculator.model.character.Player;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.perks.Perk;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player
{
	private int strength;
	private int perception;
	private int endurance;
	private int charisma;
	private int intelligence;
	private int agility;
	private int luck;
	private int level;
	private List<Perk> perks;
}

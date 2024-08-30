package Tekiz._DPSCalculator._DPSCalculator.model.character;

import Tekiz._DPSCalculator._DPSCalculator.model.character.perk.Perk;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

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

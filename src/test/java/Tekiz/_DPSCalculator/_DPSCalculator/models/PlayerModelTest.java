package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PlayerModelTest extends BaseTestClass
{
	@Test
	public void testPlayerHealth()
	{
		Player player = new Player();

		//bonus HP and endurance = 0
		assertEquals(250, player.getCurrentHP());

		//increasing player endurance by 8
		player.getSpecials().setSpecial(Specials.ENDURANCE, 9);
		player.setMaxHP(0, 0);
		assertEquals(290, player.getMaxHP());

		//setting the players hp to maxHP
		player.setCurrentHP(290);
		//reducing max health
		player.getSpecials().setSpecial(Specials.ENDURANCE, 7);
		player.setMaxHP(0,0);
		assertEquals(280, player.getMaxHP());

		//trying to set hp above max hp
		player.setCurrentHP(300);
		assertEquals(player.getCurrentHP(), player.getMaxHP());

		//setting health to 50%
		player.setCurrentHP(140);
		assertEquals(50.0, player.getHealthPercentage());

		//setting health to 25%
		player.setCurrentHP(70);
		assertEquals(25.0, player.getHealthPercentage());
	}
}

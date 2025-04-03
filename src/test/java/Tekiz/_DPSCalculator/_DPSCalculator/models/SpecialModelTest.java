package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.player.Specials;
import Tekiz._DPSCalculator._DPSCalculator.model.player.Special;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class SpecialModelTest extends BaseTestClass
{
	@Test
	public void testPlayerSpecials_WithIndividualSpecials()
	{
		//max is 15, min is 1
		Special special = new Special(1,1,1,1,1,1,1,0,0,0,0,0,0,0);

		//testing setting a special
		special.setSpecial(Specials.AGILITY, 2);
		special.setSpecial(Specials.CHARISMA, 3);

		assertEquals(2, special.getAgility());
		assertEquals(3, special.getCharisma());
		assertEquals(1, special.getIntelligence());

		//testing setting some below 1
		special.setSpecial(Specials.PERCEPTION, -1);
		special.setSpecial(Specials.LUCK, -15);
		special.setSpecial(Specials.CHARISMA, -2);

		assertEquals(1, special.getPerception());
		assertEquals(1, special.getLuck());
		assertEquals(1, special.getCharisma());

		//setting above limit
		special.setSpecial(Specials.AGILITY, 20);
		special.setSpecial(Specials.CHARISMA, 50);
		assertEquals(15, special.getAgility());
		assertEquals(15, special.getCharisma());
	}

	@Test
	public void testPlayerSpecials_WithAllSpecials()
	{
		Special special = new Special(1,1,1,1,1,1,1, 0,0,0,0,0,0,0);
		special.setAllSpecials(2,7,4,6,2,3,1);

		assertEquals(3, special.getAgility());
		assertEquals(7, special.getPerception());

		//setting some below limit
		special.setAllSpecials(-2,3,-3,6,2,3,1);
		assertEquals(1, special.getStrength());
		assertEquals(1, special.getEndurance());
		assertEquals(6, special.getCharisma());

		//setting some above the limit
		special.setAllSpecials(1,3,3,6,16,3,26);
		assertEquals(15, special.getIntelligence());
		assertEquals(15, special.getLuck());
		assertEquals(3, special.getEndurance());

		//mix of below and above
		special.setAllSpecials(-1,33,4,-6,16,32,-2);
		assertEquals(1, special.getStrength());
		assertEquals(15, special.getPerception());
		assertEquals(4, special.getEndurance());
		assertEquals(1, special.getCharisma());
		assertEquals(15, special.getIntelligence());
		assertEquals(15, special.getAgility());
		assertEquals(1, special.getLuck());
	}

	@Test
	public void testPlayerSpecials_GetSpecials()
	{
		Special special = new Special(10,2,3,4,8,5,1, 0,0,0,0,0,0,0);

		assertEquals(10, special.getSpecialValue(Specials.STRENGTH, false));
		assertEquals(2, special.getSpecialValue(Specials.PERCEPTION, false));
		assertEquals(3, special.getSpecialValue(Specials.ENDURANCE, false));
		assertEquals(4, special.getSpecialValue(Specials.CHARISMA, false));
		assertEquals(8, special.getSpecialValue(Specials.INTELLIGENCE, false));
		assertEquals(5, special.getSpecialValue(Specials.AGILITY, false));
		assertEquals(1, special.getSpecialValue(Specials.LUCK, false));
	}
}

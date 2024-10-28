package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.player.*;
import org.springframework.stereotype.Service;

/**
 * A service to map a {@link Player} object to a {@link PlayerDTO} object.
 */
@Service
public class PlayerMapper
{
	/**
	 * A method used to convert a player into a player DTO.
	 * @param player {@link Player} The player to convert.
	 * @return A {@link PlayerDTO} object.
	 */
	public PlayerDTO convertPlayerToPlayerDTO(Player player){
		Special special = player.getSpecials();
		SpecialDTO specialDTO = new SpecialDTO(special.getStrength(), special.getPerception(), special.getEndurance(),
			special.getCharisma(), special.getIntelligence(), special.getAgility(), special.getLuck());

		return new PlayerDTO(player.getMaxHP(), player.getCurrentHP(), specialDTO);
	}
}

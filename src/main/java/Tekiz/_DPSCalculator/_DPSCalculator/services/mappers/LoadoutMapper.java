package Tekiz._DPSCalculator._DPSCalculator.services.mappers;

import Tekiz._DPSCalculator._DPSCalculator.model.loadout.Loadout;
import Tekiz._DPSCalculator._DPSCalculator.model.loadout.LoadoutDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.PerkDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.player.PlayerDTO;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.dto.WeaponDetailsDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service used to convert {@link Loadout} objects into {@link LoadoutDTO}.
 */
@Service
public class LoadoutMapper
{
	private final PerkMapper perkMapper;
	private final WeaponMapper weaponMapper;
	private final PlayerMapper playerMapper;

	@Autowired
	public LoadoutMapper(PerkMapper perkMapper, WeaponMapper weaponMapper, PlayerMapper playerMapper)
	{
		this.perkMapper = perkMapper;
		this.weaponMapper = weaponMapper;
		this.playerMapper = playerMapper;
	}

	/**
	 * A method used to convert a loadout into a loadout DTO.
	 * @param loadout The loadout to convert.
	 * @return A loadout DTO.
	 */
	public LoadoutDTO convertLoadoutToLoadoutDTO(Loadout loadout)
	{
		WeaponDetailsDTO weaponDTO = weaponMapper.convertToDetailsDTO(loadout.getWeapon());
		List<PerkDTO> perkDTOs = perkMapper.convertAllToDTO(loadout.getPerks());
		PlayerDTO playerDTO = playerMapper.convertPlayerToPlayerDTO(loadout.getPlayer());

		return new LoadoutDTO(loadout.getLoadoutID(), weaponDTO, perkDTOs, playerDTO);
	}

	/**
	 * A method to convert a list of loadouts into a list of {@link LoadoutDTO}s.
	 * @param loadouts A {@link List} of {@link Loadout} objects.
	 * @return A {@link List} of {@link LoadoutDTO}s.
	 */
	public List<LoadoutDTO> covertAllLoadoutsToDTOs(Set<Loadout> loadouts)
	{
		return loadouts
			.stream()
			.map(this::convertLoadoutToLoadoutDTO).collect(Collectors.toList());
	}
}

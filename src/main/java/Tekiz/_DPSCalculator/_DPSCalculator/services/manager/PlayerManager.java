package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.config.scope.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * A service used to manage {@link Player} objects.
 */
@Service
@Scope(scopeName = "loadout")
@Getter
public class PlayerManager implements LoadoutScopeClearable
{
	private Player player;

	/**
	 * The constructor of a {@link PlayerManager} object.
	 */
	@Autowired
	public PlayerManager()
	{
		this.player = new Player();
	}

	/**
	 * A method used during the cleanup of this service.
	 */
	@PreDestroy
	public void clear() {
		player = null;
	}

}

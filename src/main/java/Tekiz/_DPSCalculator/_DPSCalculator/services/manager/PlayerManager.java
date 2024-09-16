package Tekiz._DPSCalculator._DPSCalculator.services.manager;

import Tekiz._DPSCalculator._DPSCalculator.model.player.Player;
import Tekiz._DPSCalculator._DPSCalculator.config.LoadoutScopeClearable;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "loadout")
@Getter
public class PlayerManager implements LoadoutScopeClearable
{
	private Player player;
	@Autowired
	public PlayerManager()
	{
		this.player = new Player();
	}

	@PreDestroy
	public void clear() {
		player = null;
	}

}

package Tekiz._DPSCalculator._DPSCalculator.model.loadout;

import Tekiz._DPSCalculator._DPSCalculator.model.character.Enemy.Enemy;
import Tekiz._DPSCalculator._DPSCalculator.model.character.Player.Player;
import Tekiz._DPSCalculator._DPSCalculator.model.environment.Environment;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ConsumableLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.ModLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.PerkLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.WeaponLoaderService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class Loadout
{
	private final Player player;
	private final Environment environment;
	private final Modifiers modifiers;

	private Weapon weapon;
	private Enemy enemy;

	private final WeaponLoaderService weaponLoaderService;
	private final ModLoaderService modLoaderService;
	private final PerkLoaderService perkLoaderService;
	private final ConsumableLoaderService consumableLoaderService;
	@Autowired
	public Loadout(WeaponLoaderService weaponLoaderService, PerkLoaderService perkLoaderService, ModLoaderService modLoaderService, ConsumableLoaderService consumableLoaderService)
	{
		this.weaponLoaderService = weaponLoaderService;
		this.perkLoaderService = perkLoaderService;
		this.modLoaderService = modLoaderService;
		this.consumableLoaderService = consumableLoaderService;

		this.player = new Player();
		this.environment = new Environment();
		this.modifiers = new Modifiers();
		this.weapon = null;
		this.enemy = null;
	}

	//todo - change consumables to reflect player changes
	public void setWeapon(String weaponName) throws IOException
	{
		Weapon loadedWeapon = weaponLoaderService.getWeapon(weaponName);
		if (loadedWeapon == null) {
			throw new IllegalArgumentException("Weapon not found: " + weaponName);
		}
		this.weapon = loadedWeapon;
	}

	public void calculateDamagePerSecond()
	{
		//todo - consider whether to use a different service to calculate based on type (sneaking, ranged, melee)
		//determines what damage is loaded in which order.
		//todo - change weapon damage
		double baseDamage = getWeapon().getBaseDamage(0);
		List<Double> additiveDamage = new ArrayList<>();
		List<Double> multiplicativeDamage = new ArrayList<>();

	}
}

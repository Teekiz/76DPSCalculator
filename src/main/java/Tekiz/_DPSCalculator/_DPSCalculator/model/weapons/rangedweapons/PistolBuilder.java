package Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.DamageType;
import Tekiz._DPSCalculator._DPSCalculator.model.enums.WeaponType;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.rangedweapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.util.deserializer.ReceiverDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;

@JsonDeserialize(builder = PistolBuilder.class)
public class PistolBuilder extends RangedBuilder<PistolBuilder>
{

	private Receiver receiver;
	public PistolBuilder(String weaponName, WeaponType weaponType, DamageType damageType, HashMap<Integer, Double> weaponDamageByLevel, int apCost)
	{
		super(weaponName, weaponType, damageType, weaponDamageByLevel, apCost);
	}

	@JsonProperty("receiver")
	@JsonDeserialize(using = ReceiverDeserializer.class)
	public PistolBuilder setReceiver(Receiver receiver)
	{
		this.receiver = receiver;
		return self();
	}

	@Override
	protected PistolBuilder self()
	{
		return this;
	}

	@Override
	public Pistol build()
	{
		return new Pistol(weaponName, weaponType, damageType, weaponDamageByLevel, apCost, magazineSize, fireRate,
			range, accuracy, projectileCount, criticalBonus, rangedPenalty, reloadTime, attackSpeed, attackDelay, receiver);
	}
}

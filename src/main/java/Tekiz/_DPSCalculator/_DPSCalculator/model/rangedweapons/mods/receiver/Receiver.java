package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver;
public class Receiver
{
	public Receivers receivers;
	public Ammo ammoType;

	public Receiver(Receivers receivers, Ammo ammoType)
	{
		this.receivers = receivers;
		//the ammo type is set by the gun, unless the receiver is changed, in which case, ignore the default ammo type
		if (receivers.isOverrideAmmo())
		{
			ammoType = receivers.getOverrideAmmoType();
		}
		else
		{
			this.ammoType = ammoType;
		}
	}
}

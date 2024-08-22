package Tekiz._DPSCalculator._DPSCalculator.model.rangedweapons.mods.receiver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Getter
@Repository @RequiredArgsConstructor
public class Receiver
{
	//default set to a 10mm automatic receiver
	private Receivers receivers = Receivers.AUTOMATIC;
	private Ammo ammoType = Ammo.R10MM;

	//when a receiver has its ammo set or the receiver is updated, also check to see if the ammo type needs to be updated
	public void setAmmo(Ammo ammoType)
	{
		this.ammoType = ammoType;
		overrideAmmo();
	}

	public void setReceiver(Receivers receiver)
	{
		this.receivers = receiver;
		overrideAmmo();
	}
	public void overrideAmmo()
	{
		//the ammo type is set by the gun, unless the receiver is changed, in which case, ignore the default ammo type
		if (receivers.isOverrideAmmo())
		{
			ammoType = receivers.getOverrideAmmoType();
		}
	}


}

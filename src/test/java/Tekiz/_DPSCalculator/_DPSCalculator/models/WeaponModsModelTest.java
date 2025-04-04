package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class WeaponModsModelTest extends BaseTestClass
{
	@Autowired
	DataLoaderService dataLoaderService;
	@Autowired
	WeaponFactory weaponFactory;

	String _10MMPISTOL;
	String AUTOMATIC;
	String CALIBRATE;
	String NOCHANGEDEFAULT;


	@BeforeEach
	public void initializeVariables()
	{
		_10MMPISTOL = jsonIDMapper.getIDFromFileName("10MMPISTOL");
		AUTOMATIC = jsonIDMapper.getIDFromFileName("AUTOMATIC");
		CALIBRATE = jsonIDMapper.getIDFromFileName("CALIBRATE");
		NOCHANGEDEFAULT = jsonIDMapper.getIDFromFileName("NOCHANGEDEFAULT");
	}

	@Test
	public void testChangingModification_withValidMod() throws IOException
	{
		//DEFAULT IS AUTOMATIC
		RangedWeapon rangedWeapon = (RangedWeapon) dataLoaderService.loadData(_10MMPISTOL, Weapon.class, weaponFactory);
		Receiver receiver = dataLoaderService.loadData(CALIBRATE, Receiver.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(receiver);
		assertEquals("TestCalibrated", rangedWeapon.getReceiver().getCurrentModification().name());
	}

	@Test
	public void testChangingModification_modIsNotAccepted() throws IOException
	{
		//DEFAULT IS AUTOMATIC
		RangedWeapon rangedWeapon = (RangedWeapon) dataLoaderService.loadData(_10MMPISTOL, Weapon.class, weaponFactory);
		Receiver receiver = dataLoaderService.loadData(NOCHANGEDEFAULT, Receiver.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(receiver);
		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
	}

	@Test
	public void testChangingModification_modCannotBeChanged() throws IOException
	{
		//DEFAULT IS AUTOMATIC
		Receiver defaultReceiver = dataLoaderService.loadData(AUTOMATIC, Receiver.class, null);
		RangedWeapon rangedWeapon = RangedWeapon.builder().receiver(new ModificationSlot<>(defaultReceiver, false, Set.of("AUTOMATIC", "CALIBRATE"))).build();
		Receiver receiver = dataLoaderService.loadData(CALIBRATE, Receiver.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(receiver);
		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
	}
}

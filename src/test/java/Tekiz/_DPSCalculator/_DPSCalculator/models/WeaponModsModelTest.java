package Tekiz._DPSCalculator._DPSCalculator.models;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.mods.ModType;
import Tekiz._DPSCalculator._DPSCalculator.model.mods.ModificationSlot;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.RangedWeapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import Tekiz._DPSCalculator._DPSCalculator.test.BaseTestClass;
import java.io.IOException;
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
		WeaponMod weaponMod = dataLoaderService.loadData(CALIBRATE, WeaponMod.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(weaponMod);
		assertEquals("TestCalibrated", rangedWeapon.getReceiver().getCurrentModification().name());
	}

	@Test
	public void testChangingModification_modIsNotAccepted() throws IOException
	{
		//DEFAULT IS AUTOMATIC
		RangedWeapon rangedWeapon = (RangedWeapon) dataLoaderService.loadData(_10MMPISTOL, Weapon.class, weaponFactory);
		WeaponMod weaponMod = dataLoaderService.loadData(NOCHANGEDEFAULT, WeaponMod.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(weaponMod);
		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
	}

	@Test
	public void testChangingModification_modCannotBeChanged() throws IOException
	{
		//DEFAULT IS AUTOMATIC
		WeaponMod defaultWeaponMod = dataLoaderService.loadData(AUTOMATIC, WeaponMod.class, null);
		RangedWeapon rangedWeapon = RangedWeapon.builder().receiver(new ModificationSlot<>(defaultWeaponMod, ModType.RECEIVER, false, Set.of("AUTOMATIC", "CALIBRATE"))).build();
		WeaponMod weaponMod = dataLoaderService.loadData(CALIBRATE, WeaponMod.class, null);

		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
		rangedWeapon.setMod(weaponMod);
		assertEquals("TestAutomatic", rangedWeapon.getReceiver().getCurrentModification().name());
	}
}

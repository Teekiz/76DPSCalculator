package Tekiz._DPSCalculator._DPSCalculator.util.upload;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.ArmourMod;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.WeaponMod;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourModRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ConsumableRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.MutationRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.PerkRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.WeaponModRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.WeaponRepository;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.factory.WeaponFactory;
import Tekiz._DPSCalculator._DPSCalculator.services.creation.loading.DataLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalToDBUploader
{
	private final ArmourModRepository armourModRepository;
	private final ArmourRepository armourRepository;
	private final ConsumableRepository consumableRepository;
	private final MutationRepository mutationRepository;
	private final PerkRepository perkRepository;
	private final WeaponModRepository weaponModRepository;
	private final DataLoaderService dataLoaderService;
	private final WeaponRepository weaponRepository;
	private final WeaponFactory weaponFactory;


	@Autowired
	public LocalToDBUploader(ArmourModRepository armourModRepository, ArmourRepository armourRepository,
							 ConsumableRepository consumableRepository, MutationRepository mutationRepository,
							 PerkRepository perkRepository, WeaponModRepository weaponModRepository,
							 DataLoaderService dataLoaderService, WeaponRepository weaponRepository, WeaponFactory weaponFactory)
	{
		this.armourModRepository = armourModRepository;
		this.armourRepository = armourRepository;
		this.consumableRepository = consumableRepository;
		this.weaponRepository = weaponRepository;
		this.mutationRepository = mutationRepository;
		this.perkRepository = perkRepository;
		this.weaponModRepository = weaponModRepository;
		this.dataLoaderService = dataLoaderService;
		this.weaponFactory = weaponFactory;
	}

	public void save()
	{
		try
		{
			log.debug("Saving");
			armourModRepository.saveAll(dataLoaderService.loadAllData("modArmourMaterials", ArmourMod.class, null));
			armourRepository.saveAll(dataLoaderService.loadAllData("armour", Armour.class, null));
			weaponModRepository.saveAll(dataLoaderService.loadAllData("modReceivers", WeaponMod.class, null));
			consumableRepository.saveAll(dataLoaderService.loadAllData("consumables", Consumable.class, null));
			perkRepository.saveAll(dataLoaderService.loadAllData("perks", Perk.class, null));
			mutationRepository.saveAll( dataLoaderService.loadAllData("mutations", Mutation.class, null));
			weaponRepository.saveAll(dataLoaderService.loadAllData("weapons", Weapon.class, weaponFactory));

		} catch (Exception e)
		{
			log.debug("There was an issue.", e);
		}
	}

}

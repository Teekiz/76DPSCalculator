package Tekiz._DPSCalculator._DPSCalculator.util.upload;

import Tekiz._DPSCalculator._DPSCalculator.model.armour.Armour;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Material;
import Tekiz._DPSCalculator._DPSCalculator.model.armour.mods.Miscellaneous;
import Tekiz._DPSCalculator._DPSCalculator.model.consumables.Consumable;
import Tekiz._DPSCalculator._DPSCalculator.model.mutations.Mutation;
import Tekiz._DPSCalculator._DPSCalculator.model.perks.Perk;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.Weapon;
import Tekiz._DPSCalculator._DPSCalculator.model.weapons.mods.Receiver;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourMaterialRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourMiscellaniousRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ArmourRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ConsumableRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.MutationRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.PerkRepository;
import Tekiz._DPSCalculator._DPSCalculator.persistence.ReceiverRepository;
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
	private final ArmourMaterialRepository armourMaterialRepository;
	private final ArmourMiscellaniousRepository armourMiscellaniousRepository;
	private final ArmourRepository armourRepository;
	private final ConsumableRepository consumableRepository;
	private final MutationRepository mutationRepository;
	private final PerkRepository perkRepository;
	private final ReceiverRepository receiverRepository;
	private final DataLoaderService dataLoaderService;
	private final WeaponRepository weaponRepository;
	private final WeaponFactory weaponFactory;


	@Autowired
	public LocalToDBUploader(ArmourMaterialRepository armourMaterialRepository, ArmourMiscellaniousRepository armourMiscellaniousRepository, ArmourRepository armourRepository,
							 ConsumableRepository consumableRepository, MutationRepository mutationRepository,
							 PerkRepository perkRepository, ReceiverRepository receiverRepository,
							 DataLoaderService dataLoaderService, WeaponRepository weaponRepository, WeaponFactory weaponFactory)
	{
		this.armourMaterialRepository = armourMaterialRepository;
		this.armourMiscellaniousRepository = armourMiscellaniousRepository;
		this.armourRepository = armourRepository;
		this.consumableRepository = consumableRepository;
		this.weaponRepository = weaponRepository;
		this.mutationRepository = mutationRepository;
		this.perkRepository = perkRepository;
		this.receiverRepository = receiverRepository;
		this.dataLoaderService = dataLoaderService;
		this.weaponFactory = weaponFactory;
	}

	public void save()
	{
		try
		{
			log.debug("Saving");
			armourMaterialRepository.saveAll(dataLoaderService.loadAllData("modArmourMaterials", Material.class, null));
			armourMiscellaniousRepository.saveAll(dataLoaderService.loadAllData("modArmourMisc", Miscellaneous.class, null));
			armourRepository.saveAll(dataLoaderService.loadAllData("armour", Armour.class, null));
			receiverRepository.saveAll(dataLoaderService.loadAllData("modReceivers", Receiver.class, null));
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

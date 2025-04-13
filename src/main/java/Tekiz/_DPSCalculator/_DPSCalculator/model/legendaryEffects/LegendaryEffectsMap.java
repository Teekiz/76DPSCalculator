package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.legendaryEffects.StarType;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * An object representing the legendary effects a piece of equipment has.
 */
public class LegendaryEffectsMap extends HashMap<LegendaryEffect, Boolean>
{
	/**
	 * A {@link HashMap} for legendary effects. If set to {@code true}, the effect can be changed.
	 */
	public LegendaryEffectsMap() {
		super();
	}

	/**
	 * A method to add a new legendary effect if the current slot can be modified.
	 *
	 * @param newEffect The new legendary effect to be added.
	 * @return {@code true} if the effect was added successfully.
	 */
	public boolean addLegendaryEffect(LegendaryEffect newEffect)
	{
		if (newEffect == null) {
			return false;
		}

		LegendaryEffect currentEffectInSlot = this.keySet().stream()
			.filter(Objects::nonNull)
			.filter(e -> e.starType().equals(newEffect.starType())).findFirst().orElse(null);

		if (currentEffectInSlot == null)
		{
			this.put(newEffect, true);
			return true;
		}
		else if (Boolean.TRUE.equals(this.get(currentEffectInSlot)))
		{
			this.remove(currentEffectInSlot);
			this.put(newEffect, true);
		}

		return true;
	}

	/**
	 * A method to remove an existing legendary effect if the current slot can be modified.
	 *
	 * @param starType The legendary star to be removed.
	 */
	public boolean removeLegendaryEffect(StarType starType)
	{
		if (starType == null){
			return false;
		}
		LegendaryEffect currentEffectInSlot = this.keySet().stream()
			.filter(Objects::nonNull)
			.filter(e -> e.starType().equals(starType)).findFirst().orElse(null);

		if (currentEffectInSlot != null && get(currentEffectInSlot))
		{
			this.remove(currentEffectInSlot);
			return true;
		}
		return false;
	}

	/**
	 * A method that gets all the effects and coverts them into a {@link List}.
	 * @return A {@link List} of {@link LegendaryEffect} objects.
	 */
	public List<LegendaryEffect> getAllEffects(){
		return this.keySet().stream().toList();
	}
}



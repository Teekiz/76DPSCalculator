package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import java.util.HashMap;
import java.util.List;

/**
 * An object representing the legendary effects a piece of equipment has.
 */
public class LegendaryEffectsMap
{
	/**
	 * A {@link HashMap} for legendary effects. If set to {@code true}, the effect can be changed.
	 */
	private final HashMap<LegendaryEffect, Boolean> legendaryEffects;

	public LegendaryEffectsMap()
	{
		this.legendaryEffects = new HashMap<>();
	}

	public LegendaryEffectsMap(HashMap<LegendaryEffect, Boolean> legendaryEffects)
	{
		this.legendaryEffects = legendaryEffects;
	}

	/**
	 * A method to return all the legendary effects on an object and convert them into a {@link HashMap} for aggregation.
	 * @return A {@link List} of {@link LegendaryEffect}'s currently contained on an object.
	 */
	public HashMap<LegendaryEffect, Boolean> aggregateLegendaryEffectsModifiers()
	{
		HashMap<LegendaryEffect, Boolean> newEffectsMap = new HashMap<>();
		legendaryEffects.keySet().forEach(e -> newEffectsMap.put(e, true));
		return newEffectsMap;
	}

	/**
	 * A method to add a new legendary effect if the current slot can be modified.
	 *
	 * @param newEffect The new legendary effect to be added.
	 */
	public void addLegendaryEffect(LegendaryEffect newEffect)
	{
		LegendaryEffect currentEffectInSlot = legendaryEffects.keySet().stream()
			.filter(e -> e.starType().equals(newEffect.starType())).findFirst().orElse(null);

		if (currentEffectInSlot == null)
		{
			legendaryEffects.put(newEffect, true);
		}
		else if (Boolean.TRUE.equals(legendaryEffects.get(currentEffectInSlot)))
		{
			legendaryEffects.remove(currentEffectInSlot);
			legendaryEffects.put(newEffect, true);
		}
	}

	/**
	 * A method to remove an existing legendary effect if the current slot can be modified.
	 *
	 * @param effect The new legendary effect to be removed.
	 */
	public void removeLegendaryEffect(LegendaryEffect effect)
	{
		LegendaryEffect currentEffectInSlot = legendaryEffects.keySet().stream()
			.filter(e -> e.starType().equals(effect.starType())).findFirst().orElse(null);

		if (currentEffectInSlot == null || Boolean.TRUE.equals(legendaryEffects.get(currentEffectInSlot)))
		{
			legendaryEffects.remove(effect);
		}
	}
}



package Tekiz._DPSCalculator._DPSCalculator.model.legendaryEffects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Objects;

/**
 * An object representing the legendary effects a piece of equipment has.
 */
public class LegendaryEffectsMap extends HashMap<LegendaryEffect, Boolean>
{
	/**
	 * A {@link HashMap} for legendary effects. If set to {@code true}, the effect can be changed.
	 */

	/*
	@JsonCreator
	public LegendaryEffectsMap(@JsonProperty("legendaryEffects") HashMap<LegendaryEffect, Boolean> legendaryEffects) {
		if (legendaryEffects != null) {
			this.putAll(legendaryEffects);
		}
	}

	 */

	public LegendaryEffectsMap() {
		super();
	}

	/**
	 * A method to add a new legendary effect if the current slot can be modified.
	 *
	 * @param newEffect The new legendary effect to be added.
	 */
	public void addLegendaryEffect(LegendaryEffect newEffect)
	{
		if (newEffect == null) {
			return;
		}

		LegendaryEffect currentEffectInSlot = this.keySet().stream()
			.filter(Objects::nonNull)
			.filter(e -> e.starType().equals(newEffect.starType())).findFirst().orElse(null);

		if (currentEffectInSlot == null)
		{
			this.put(newEffect, true);
		}
		else if (Boolean.TRUE.equals(this.get(currentEffectInSlot)))
		{
			this.remove(currentEffectInSlot);
			this.put(newEffect, true);
		}
	}

	/**
	 * A method to remove an existing legendary effect if the current slot can be modified.
	 *
	 * @param effect The new legendary effect to be removed.
	 */
	public void removeLegendaryEffect(LegendaryEffect effect)
	{
		if (effect == null) {
			return;
		}

		LegendaryEffect currentEffectInSlot = this.keySet().stream()
			.filter(Objects::nonNull)
			.filter(e -> e.starType().equals(effect.starType())).findFirst().orElse(null);

		if (currentEffectInSlot == null || Boolean.TRUE.equals(this.get(currentEffectInSlot)))
		{
			this.remove(effect);
		}
	}
}



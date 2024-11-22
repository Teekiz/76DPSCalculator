package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

import lombok.Getter;

@Getter
public class ModifierValue<T>
{
	private final T value;

	public ModifierValue(ModifierTypes type, T value){
		if (!type.getValueType().isInstance(value)) {
			throw new IllegalArgumentException(
				"Invalid type for " + type + ": Expected " + type.getValueType().getSimpleName()
			);
		}
		this.value = value;
	}
}

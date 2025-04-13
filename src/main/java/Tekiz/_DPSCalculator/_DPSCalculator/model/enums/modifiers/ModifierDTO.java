package Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers;

import lombok.Getter;

@Getter
public class ModifierDTO<T>
{
	@SuppressWarnings("unchecked")
	public ModifierDTO(ModifierTypes type, ModifierValue<?> modifierValue){
		this.type = type;
		this.value = (T) modifierValue.getValue();

		if (type.getDisplayName().isBlank()) {
			userDescription = "NOT APPLICABLE";
			return;
		}

		String prefix = "";
		String valueStr = modifierValue.toString();

		if (value instanceof Number) {
			//if the number is a double, multiply by 100 (0.2 -> 20)
			int numberValue = (value instanceof Double doubleValue)
				? (int) (doubleValue * 100)
				: ((Number) value).intValue();

			// If the values need to be changed around due to user readability, then this will swap the values
			if (type.getInverseValue()) {
				prefix = (numberValue < 0) ? "+" : "-";
			} else {
				prefix = (numberValue >= 0) ? "+" : "";
			}
			valueStr = String.valueOf(Math.abs(numberValue));
		}

		this.userDescription = type.getDisplayName() + ": " + prefix + valueStr + "%.";
	}

	ModifierTypes type;
	T value;
	String userDescription;
}

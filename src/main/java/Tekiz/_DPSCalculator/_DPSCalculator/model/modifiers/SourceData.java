package Tekiz._DPSCalculator._DPSCalculator.model.modifiers;

import Tekiz._DPSCalculator._DPSCalculator.model.enums.modifiers.BonusTypes;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SourceData
{
	private Object sourceObject;
	private BonusTypes bonusTypes;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SourceData that = (SourceData) o;
		return Objects.equals(sourceObject, that.sourceObject) &&
			bonusTypes == that.bonusTypes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceObject, bonusTypes);
	}
}

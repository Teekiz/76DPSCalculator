package Tekiz._DPSCalculator._DPSCalculator.services.creation.factory;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Factory<T, A>
{
	T createObject(A argument, String id);
}

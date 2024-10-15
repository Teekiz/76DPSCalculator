package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import java.io.Serializable;

public interface Keyable extends Serializable
{
	int id();
	String name();
}

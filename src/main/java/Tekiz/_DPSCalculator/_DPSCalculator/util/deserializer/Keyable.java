package Tekiz._DPSCalculator._DPSCalculator.util.deserializer;

import java.io.Serializable;
import java.util.HashMap;

/**
 * An interface used by {@link HashMapKeyComponent}
 * to serialize and deserialize an object if there are the key of a {@link HashMap}.
 */
public interface Keyable extends Serializable
{
	int id();
	String name();
}

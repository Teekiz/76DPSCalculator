package Tekiz._DPSCalculator._DPSCalculator.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to denote when the {@link LoadoutAspect} should intercept. This will be used when the method has been called.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SaveLoadout
{
}

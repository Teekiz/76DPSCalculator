package Tekiz._DPSCalculator._DPSCalculator.model.exceptions;

/** An exception to be thrown when an object is not loaded correctly. */
public class ResourceNotFoundException extends Exception
{
	public ResourceNotFoundException(){}
	public ResourceNotFoundException(String message){
		super(message);
	}
}

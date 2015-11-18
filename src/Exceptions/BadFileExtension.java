package Exceptions;

public class BadFileExtension extends Exception {
	
	public BadFileExtension (String message){
		super(message);
	}
	
	public BadFileExtension(String message, Throwable throwable){
		super(message, throwable);
	}
}

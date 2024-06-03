package dse.datafeeder.exception;

public class NotFoundException extends RuntimeException{
    // Thrown when an element is not found.
    public NotFoundException(String message){super(message);}
}

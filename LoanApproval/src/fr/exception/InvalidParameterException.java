package fr.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class InvalidParameterException extends WebApplicationException{
	
	private static final long serialVersionUID = 8226670505753241020L;

	public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
    	
         super(Response.status(Response.Status.UNAUTHORIZED).entity(message).type("text/plain").build());
    }

}

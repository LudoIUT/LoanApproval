package fr.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ResourceNotFoundException extends WebApplicationException {
	
	private static final long serialVersionUID = 8226670505753241020L;

	public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
    	
         super(Response.status(Response.Status.NOT_FOUND).entity(message).type("text/plain").build());
    }
   
}
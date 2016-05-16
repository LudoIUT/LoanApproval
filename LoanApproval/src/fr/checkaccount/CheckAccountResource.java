package fr.checkaccount;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import fr.exception.ResourceNotFoundException;

@Path("/check")
public class CheckAccountResource {
	
	@GET
	@Path("/getRisk/{lastName}")
	@Produces("application/json")
	public String getRisk(@PathParam("lastName") String lastName) throws JSONException
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("bankAccount", lastName);
		try{
			datastore.get(key);
		}
		
		catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("The bank account with name " + lastName + " is not found");
		}
		
		Entity account = null;
		try {
			account = datastore.get(key);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String risk = (String) account.getProperty("risk");
		String returnResult = "{\"risks\": [";
		
		JSONObject jsonApproval = new JSONObject();
		
		returnResult += jsonApproval.put("Risk", risk) + ",";
		
		returnResult = returnResult.substring(0,returnResult.length()-1);
		returnResult += "]}";
		return returnResult;
		
	}
}
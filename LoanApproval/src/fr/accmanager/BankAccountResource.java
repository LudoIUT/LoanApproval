package fr.accmanager;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import fr.exception.AlreadyExistsException;
import fr.exception.InvalidParameterException;
import fr.exception.ResourceNotFoundException;


//import exception.ResourceNotFoundException;

@Path("/account")
public class BankAccountResource {
	
	@GET
	@Path("/getAccount")
	@Produces("application/json")
	public String getAccount() throws JSONException
	{
		String returnResult = "{\"accounts\": [";
		Query q = new Query("bankAccount");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		
		if(pq.countEntities() == 0){
			 throw new ResourceNotFoundException("bank account not found in datastore.");
		}
		
		for(Entity result : pq.asIterable())
		{
			String firstName = (String)result.getProperty("firstName");
			String lastName = (String)result.getProperty("lastName");
			String account = (String)result.getProperty("account");
			String risk = (String)result.getProperty("risk");			
			
			JSONObject jsonApproval = new JSONObject();
			jsonApproval.put("Id", result.getKey().getName());
			jsonApproval.put("FirstName", firstName);
			jsonApproval.put("LastName", lastName);
			jsonApproval.put("Account", account);
			jsonApproval.put("Risk", risk);			
			
			returnResult += jsonApproval + ",";
		}
		
		returnResult = returnResult.substring(0,returnResult.length()-1); returnResult += "]}";	
		return returnResult;		
	}
	
	@POST
	@Path("/add")
	public String addAccount(@FormParam("lastName") String lastName, @FormParam("firstName") String firstName, @FormParam("account") String account, @FormParam("risk") String risk)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("bankAccount", lastName);
		try{
			datastore.get(key);
		}
		catch(EntityNotFoundException e){
		
			Entity bank = new Entity("bankAccount", lastName);
			bank.setProperty("lastName", lastName);
			bank.setProperty("firstName", firstName);
			bank.setProperty("account", account);
			bank.setProperty("risk", risk);
			
			datastore.put(bank);
			
			return "Account added";
		}
		throw new AlreadyExistsException("Already exists a bank account with this name");
	}
	
	@DELETE
	@Path("/delete/{lastName}")
	public String deleteAccount (@PathParam("lastName") String lastName){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key account = KeyFactory.createKey("bankAccount", lastName);
		try{
			datastore.get(account);
		}
		
		catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("The bank account with name " + lastName + " is not found");
		}
		datastore.delete(account);
		
		return "Account deleted";
	}
	
	@PUT
	@Path("/updateRisk/{lastName}/{risk}")
	public String updateRisk(@PathParam("lastName") String lastName, @PathParam("risk") String risk) throws JSONException
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
		if(!risk.equals("high") && !risk.equals("low"))
			throw new InvalidParameterException("The risk must be low or high");
		account.setProperty("risk", risk);
		datastore.put(account);
		return "risk updated";
	}
	
	@PUT
	@Path("/updateAccount/{lastName}/{account}")
	public String updateAccount(@PathParam("lastName") String lastName, @PathParam("account") String account) throws JSONException
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("bankAccount", lastName);
		try{
			datastore.get(key);
		} catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("The bank account with name " + lastName + " is not found");
		}
		
		Entity accountP = null;
		try {
			accountP = datastore.get(key);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("The bank account with name " + lastName + " is not found");
		}
		
		try{
		  Integer.parseInt(account);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Invalid integer for the amout");
		}
		
		accountP.setProperty("account", account);		
		datastore.put(accountP);		
		return "Account updated";		
	}
}
package fr.appmanager;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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

@Path("/approval")
public class ApprovalResource {
	
	@POST
	@Path("/add")
	public String addApproval(@FormParam("name") String name, @FormParam("response") String response)
	{
		if(!response.equals("accepted") && !response.equals("refused"))
			throw new InvalidParameterException("The response must be accepted or refused");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("Approval", name);
		try{
			datastore.get(key);
		}catch(EntityNotFoundException e){
			Entity approval = new Entity("Approval", name);
			approval.setProperty("name", name);
			approval.setProperty("response", response);
			datastore.put(approval);
			return "Approval added";
		}
		throw new AlreadyExistsException("Already exists an approval with this name");
	}
	
	@DELETE
	@Produces("text/plain")
	@Path("/delete/{name}")
	public String deleteApproval(@PathParam("name")String name)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Key key = KeyFactory.createKey("Approval", name);
		try{
			datastore.get(key);
		}
		catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("The approval with name " + name + " is not found");
		}
		datastore.delete(key);
		return "Approval deleted";
	}
	
	@GET
	@Path("/getApprovals")
	@Produces("application/json")
	public String getApprovals() throws JSONException
	{
		String returnResult = "{\"approvals\": [";
		Query q = new Query("Approval");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		
		if(pq.countEntities() == 0)
			 throw new ResourceNotFoundException("Approval not found in datastore.");
	
		for(Entity result : pq.asIterable())
		{
			String name = (String)result.getProperty("name");
			String response = (String)result.getProperty("response");
			JSONObject jsonApproval = new JSONObject();
			jsonApproval.put("Id", result.getKey().getName());
			jsonApproval.put("Name", name);
			jsonApproval.put("Response", response);
			returnResult += jsonApproval + ",";
		}
		
		returnResult = returnResult.substring(0,returnResult.length()-1);
		returnResult += "]}";
		return returnResult;
	}
}
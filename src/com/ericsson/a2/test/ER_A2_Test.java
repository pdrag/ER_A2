package com.ericsson.a2.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ER_A2_Test 
{

	static final String DB_NAME = "EricssonAssignment2";
	static final String COLLECTION_NAME = "entries";
	MongoClient mongo;
	MongoDatabase mdb;
	
	
	static Response doGet(String id)
	{
		if (id==null) id = "";
		Response response = null;
		try 
		{
			JerseyClient client = (JerseyClient) JerseyClientBuilder.newClient();
			JerseyWebTarget target = client.target("http://localhost:8080/EricssonAssignment2/restAPI/items/" + id);
			response = target.request(MediaType.APPLICATION_JSON).accept("application/json").get();
			if (response.getStatus() != 200) 
			{
			   fail("Failed : HTTP error code : " + response.getStatus());
			}
		} 
		catch (Exception e) 
		{
			fail("Exception: " + e.getMessage());
		}
		return response;
	}
	
	static Response doDelete(String id)
	{
		Response response = null;
		try 
		{
			JerseyClient client = (JerseyClient) JerseyClientBuilder.newClient();
			JerseyWebTarget target = client.target("http://localhost:8080/EricssonAssignment2/restAPI/items/"+id);
			response = target.request(MediaType.APPLICATION_JSON).accept("application/json").delete();
			if (response.getStatus() != 200) 
			{
			   fail("Failed : HTTP error code : " + response.getStatus());
			}
		} 
		catch (Exception e) 
		{
			fail("Exception: " + e.getMessage());
		}
		return response;
	}
	
	static Response doPost(Document qry)
	{
		Response response = null;
		try 
		{
			JerseyClient client = (JerseyClient) JerseyClientBuilder.newClient();
			JerseyWebTarget target = client.target("http://localhost:8080/EricssonAssignment2/restAPI/items/");
			response = target.request(MediaType.APPLICATION_JSON).accept("application/json")
					.post(Entity.entity(qry.toJson(), MediaType.APPLICATION_JSON));
			if (response.getStatus() != 200) 
			{
			   fail("Failed : HTTP error code : " + response.getStatus());
			}
		} 
		catch (Exception e) 
		{
			fail("Exception: " + e.getMessage());
		}
		return response;
	}
	
	static Response doPut(Document qry)
	{
		Response response = null;
		try 
		{
			JerseyClient client = (JerseyClient) JerseyClientBuilder.newClient();
			JerseyWebTarget target = client.target("http://localhost:8080/EricssonAssignment2/restAPI/items/");
			response = target.request(MediaType.APPLICATION_JSON).accept("application/json")
					.put(Entity.entity(qry.toJson(), MediaType.APPLICATION_JSON));
			if (response.getStatus() != 200) 
			{
			   fail("Failed : HTTP error code : " + response.getStatus());
			}
		} 
		catch (Exception e) 
		{
			fail("Exception: " + e.getMessage());
		}
		return response;
	}
	
	
	
	@Before
	public void before()
	{
		mongo = new MongoClient();
		mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		col.drop();
	}
	
	@After
	public void after()
	{
		mongo = null;
		mdb = null;
	}
	
	
	// Empty list
	@Test
	public void testEmpty() 
	{
		Response res = doGet("");
		String text = res.readEntity(String.class);
		assertTrue(text.isEmpty());
	}

	// Add new 
	@Test
	public void testAddNew() 
	{
		Document req = new Document("content", "test123");
		Response res = doPut(req);
		String text = res.readEntity(String.class);
		Document entry1 = Document.parse(text);
		assertTrue(entry1.containsKey("_id"));
		assertTrue("test123".equals(entry1.getString("content")));
		assertTrue(entry1.size()==2);
		
		Document req2 = new Document("content", "another test");
		res = doPut(req2);
		text = res.readEntity(String.class);
		Document entry2 = Document.parse(text);
		assertTrue(entry2.containsKey("_id"));
		assertTrue("another test".equals(entry2.getString("content")));
		assertTrue(entry2.size()==2);
		
		
	}

	@Test
	public void testGet() 
	{
		Document req = new Document("content", "test123");
		Response res = doPut(req);
		String text = res.readEntity(String.class);
		Document entry1 = Document.parse(text);
		assertTrue(entry1.containsKey("_id"));
		assertTrue("test123".equals(entry1.getString("content")));
		assertTrue(entry1.size()==2);
		
		Document req2 = new Document("content", "another test");
		res = doPut(req2);
		text = res.readEntity(String.class);
		Document entry2 = Document.parse(text);
		assertTrue(entry2.containsKey("_id"));
		assertTrue("another test".equals(entry2.getString("content")));
		assertTrue(entry2.size()==2);
		
		// retrieve existing 
		res = doGet(entry1.getString("_id"));
		text = res.readEntity(String.class);
		Document entry = Document.parse(text);
		assertTrue(entry.getString("_id").equals(entry1.getString("_id")));
		assertTrue("test123".equals(entry.getString("content")));
		
		// delete existing item
		res = doDelete(entry1.getString("_id"));
		text = res.readEntity(String.class);
		Document result = Document.parse(text);
		assertTrue(result.getInteger("code").equals(0));
		
		// delete unexisting item
		res = doDelete("bad ID");
		text = res.readEntity(String.class);
		result = Document.parse(text);
		assertFalse(result.getInteger("code").equals(0));
		
		// retrieve deleted 
		res = doGet(entry1.getString("_id"));
		text = res.readEntity(String.class);
		result = Document.parse(text);
		assertFalse(result.containsKey("_id"));
		assertFalse(result.getInteger("code").equals(0));
		
		// update existing 
		Document ureq = new Document("content", "test789");
		ureq.append("_id", entry2.getString("_id"));
		res = doPost(ureq);
		text = res.readEntity(String.class);
		result = Document.parse(text);
		assertTrue(result.getInteger("code").equals(0));
		
		// update deleted 
		ureq = new Document("content", "test789");
		ureq.append("_id", entry1.getString("_id"));
		res = doPost(ureq);
		text = res.readEntity(String.class);
		result = Document.parse(text);
		assertFalse(result.getInteger("code").equals(0));
		
		
		
	}

}

package com.ericsson.a2;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/items")
public class RESTService 
{

	CRUDApp app;

	public RESTService() 
	{
		app = new CRUDApp();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProducts(@Context Response response) 
	{
		List<Entry> entries = null;
		try
		{
			entries = app.getAllEntries();
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		if (entries.isEmpty())
			return Response.ok().build();
		return Response.ok(entries.toArray(new Entry[0]), MediaType.APPLICATION_JSON).build();
	}

	// URI: /items/<id>
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	@Path("{id}")
	public Response getEntryById(@PathParam("id") String id) 
	{
		Entry entry = null; 
		try
		{
			entry = app.getEntryById(id);
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		if (entry==null)
			return Response.ok(new AppError(3, "Not Found"), MediaType.APPLICATION_JSON).build();
		return Response.ok(entry, MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON )
	public Response addNewTextContent(String content) 
	{
		Entry entry = null; 
		try
		{
			entry = app.createEntry(content!=null? content : "");
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		if (entry==null)
			return Response.ok(new AppError(2, "Can't create a new entry"), MediaType.APPLICATION_JSON).build();
		return Response.ok(entry, MediaType.APPLICATION_JSON).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNew(EntryData data) 
	{
		Entry entry = null; 
		try
		{
			entry = app.createEntry(data!=null? data.getContent() : "");
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		if (entry==null)
			return Response.ok(new AppError(2, "Can't create a new entry"), MediaType.APPLICATION_JSON).build();
		return Response.ok(entry, MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEntry(Entry entry)
	{
		boolean ok; 
		try
		{
			ok = app.updateEntryById(entry);
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		if (!ok)
			return Response.ok(new AppError(3, "Not Found"), MediaType.APPLICATION_JSON).build();
		return Response.ok(new AppError(0, "Success"), MediaType.APPLICATION_JSON).build();
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response deleteEntry(@PathParam("id") String id)
	{
		
		try
		{
			app.deleteEntryById(id);
		}
		catch (Exception e)
		{
			// return Response.serverError().entity("Exception caught: " + e.getMessage() ).build();
			return Response.ok(new AppError(e), MediaType.APPLICATION_JSON).build();
		}
		return Response.ok(new AppError(0, "Success"), MediaType.APPLICATION_JSON).build();
	}
	
//	@Context
//	UriInfo uriInfo;
//	
//	@Context
//	Request request;


}
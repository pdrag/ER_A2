package com.ericsson.a2;

import org.bson.Document;

public class Entry 
{
	String _id;
	String content;
	
	Entry()
	{
		
	}
	
	Entry(String content)
	{
		this.content = content;
	}
	
	Entry(String id, String content) 
	{
		this._id = id;
		this.content = content;
	}

	public Entry(Document dbo) 
	{
		_id = dbo.getObjectId("_id").toString();
		content = dbo.getString("content");
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public String get_id() 
	{
		return _id;
	}

	public void set_id(String _id) 
	{
		this._id = _id;
	}

}

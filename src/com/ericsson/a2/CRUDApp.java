package com.ericsson.a2;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CRUDApp  
{
	static final String DB_NAME = "EricssonAssignment2";
	static final String COLLECTION_NAME = "entries";
	
	MongoClient mongo = new MongoClient();
	
	List<Entry> getAllEntries()
	{
		MongoDatabase mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		FindIterable<Document> dblist = col.find();
		List<Entry> list = new ArrayList<Entry>();
		for (Document dbo : dblist)
			list.add(new Entry(dbo));
		return list;
	}
	
	Entry getEntryById(String id)
	{
		MongoDatabase mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		Document qry = new Document("_id", new ObjectId(id));
		FindIterable<Document> dbres = col.find(qry);
		Document dbo = dbres.first();
		return dbo!=null? new Entry(dbo) : null;
	}
	
	// returns false if it does not exist
	boolean updateEntryById(Entry entry)
	{
		MongoDatabase mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		Document qry = new Document("_id", new ObjectId(entry.get_id()));
		Document dbo = new Document("_id", new ObjectId(entry.get_id())).append("content", entry.getContent());
		Document dbres = col.findOneAndReplace(qry, dbo);
		return dbres!=null;
	}
	
	void deleteEntryById(String id)
	{
		MongoDatabase mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		Document dbo = new Document("_id", new ObjectId(id));
		col.deleteOne(dbo);
	}
	
	Entry createEntry(String content)
	{
		MongoDatabase mdb = mongo.getDatabase(DB_NAME);
		MongoCollection<Document> col = mdb.getCollection(COLLECTION_NAME);
		Document dbo = new Document("content", content!=null? content : "");
		col.insertOne(dbo);
		return new Entry(dbo);
	}
	
}

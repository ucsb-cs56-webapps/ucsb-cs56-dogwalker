package edu.ucsb.cs56.pconrad;

import com.fasterxml.jackson.core.JsonParseException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
	
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mongodb.client.model.ReturnDocument.AFTER;
import static com.mongodb.client.model.ReturnDocument.BEFORE;
import com.mongodb.client.model.FindOneAndReplaceOptions;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.apache.log4j.Logger;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;

// In a real application you may want to use a DB, for this example we just store the posts in memory

public class Model {

	private static Logger log = SparkMustacheDemo02.log;
	
    private int nextId = 1;

	private MongoClientURI uri;
    private MongoClient client;
    private MongoDatabase db;

	private MongoCollection<Document> postCollection;
	
	public Model (String uriString) {
		log.debug("Connecting to MongoDB using uriString="+uriString);
		this.uri = new MongoClientURI(uriString); 
		this.client = new MongoClient(uri);
		this.db = client.getDatabase(uri.getDatabase());
		log.debug("Connected to MongoDB, db="+this.db+" client="+this.client);

		// get a handle to the "posts" collection
        postCollection = this.db.getCollection("posts");
		
	}
    
	/**
	   Code from <a href="https://stackoverflow.com/questions/32065045/auto-increment-sequence-in-mongodb-using-java">
	   https://stackoverflow.com/questions/32065045/auto-increment-sequence-in-mongodb-using-java</a> to get next
	   sequence number from a MongoDB database.
	*/
	
	public int getNextSequence(String name) throws Exception {
		MongoCollection<Document> collection = db.getCollection("counters");
		collection.updateOne(eq("_id", name), new Document("$inc", new Document("seq", 1)));
		return collection.find(eq("_id", name)).first().getInteger("seq");
	}

	// document.put("_id", getNextSequence("userid"));
    // document.put("name","Sarah C.");
    // collection.insert(document); // insert first doc

	public int createPost(Post p) throws Exception {
	    return createPost(p.getEmail(),p.getName(),p.getNumber(),p.getDogType(),p.getBoyOrGirl(),p.getDescription(),p.getAvailability());
	}

    public int createPost(String email, String name,String number, String dogType, String boyOrGirl, String description, String availability) throws Exception {
		
		int id  = getNextSequence("postId");
		System.out.println("\n\n\n\n\n******* nextSeq = " + id + "************\n\n\n\n\n");
		
		Post post = new Post();
		post.setId(id);
		post.setName(name);
		post.setNumber(number);
		post.setEmail(email);
		post.setDogType(dogType);
		post.setBoyOrGirl(boyOrGirl);
		post.setDescription(description);
		post.setAvailability(availability);
		String json = SparkMustacheDemo02.dataToJson(post);
		System.out.println("\n\n\n\n\n******* json = " + json + "************\n\n\n\n\\n");
		postCollection.insertOne(Document.parse(json));
		
		return id;
    }
    
    public List<Post> getAllPosts(){

		List<Post> result = new ArrayList<Post>();
		
		FindIterable<Document> docsFound = postCollection.find();

		for (Document cur : docsFound) {
			try {
				String json = cur.toJson();
				log.debug("\n\n\n\n\ncur.toJson()="+json);
				Post p = SparkMustacheDemo02.json2Post(json);
				int id = cur.getInteger("id");
				p.setId(id);
				log.debug("Post p="+p);				
				result.add(p);				
			} catch (Exception e ) {
				log.error("Exception="+e);
			}
        }
		
		return result;
    }

    public Post getPost(String id){
		log.debug("****** getPost with id=" + id);
		Post result = null;

		try {
			Document cur=postCollection.find(eq("id", Integer.parseInt(id))).first();
		
			String json = cur.toJson();
			result = SparkMustacheDemo02.json2Post(json);
			int id_ = cur.getInteger("id");
			result.setId(id_);
		} catch (Exception e ) {
			log.error("Exception="+e);
		}
		return result;
    }

	public Post doc2Post(Document d) {
		Post post = null;
		try {
			String json = d.toJson();
			post = SparkMustacheDemo02.json2Post(json);			
			int id_ = d.getInteger("id");
			post.setId(id_);
		} catch (Exception e) {
			log.debug("ERROR: " + e.toString());
		}
		return post;
	}
	
    public PostUpdateResult updatePost(String id, Post newPost){
		log.debug("****** updatePost with id=" + id);
		PostUpdateResult result = null;

		try {
			newPost.setId(Integer.parseInt(id));
			Document replacementDocument =
				Document.parse(SparkMustacheDemo02.dataToJson(newPost));
			FindOneAndReplaceOptions options =
				new FindOneAndReplaceOptions().returnDocument(AFTER);
			Document oldDocument =
				postCollection.find(eq("id", Integer.parseInt(id))).first();

			Document newDocument =
				postCollection.findOneAndReplace(eq("id", Integer.parseInt(id)),
												 replacementDocument,
												 options);
			result = new PostUpdateResult(doc2Post(oldDocument),doc2Post(newDocument));
			
		} catch (Exception e ) {
			log.error("Exception="+e);
		}
		return result;
    }

	@lombok.Data
	public static class PostUpdateResult {
		private Post before;
		private Post after;
		public PostUpdateResult(Post before, Post after) {
			this.before = before;
			this.after = after;
		}
	}
	
    public boolean deletePost(String id){
		log.debug("****** deletePost with id=" + id);
		boolean result = false;

		try {
			Document cur=
				postCollection.findOneAndDelete(eq("id", Integer.parseInt(id)));
			result = (cur!=null);
		} catch (Exception e ) {
			log.error("Exception="+e);
			result = false;
		}
		return result;
    }
	
}

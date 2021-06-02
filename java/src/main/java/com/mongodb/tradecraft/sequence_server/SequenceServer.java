package com.mongodb.tradecraft.sequence_server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import static com.mongodb.client.model.Updates.inc;
import org.bson.Document;
import spark.Request;
import spark.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceServer {

    Logger logger;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private static int x=-1;

    SequenceServer() {
        mongoClient = MongoClients.create("mongodb+srv://smartek:smartekpwd@smartek.2ec6p.mongodb.net/sequencedb?retryWrites=true");
        database = mongoClient.getDatabase("sequencedb");
        collection = database.getCollection("sequenceCol");
        System.out.println("Database contains " + collection.countDocuments() + " sequences");
        logger = LoggerFactory.getLogger(SequenceServer.class);
    }

    //Fetch back inforamtion about samples
    public String sequence(Request req, Response res) {
        res.type("application/json");
        String name = req.params(":name");
         x=(x+1)%10;
         name=name.concat("_"+x);
//        Integer counter = updateDocumentAndgetCounter(name);
        
        String json = "{" + name + ":" + updateDocumentAndgetCounter(name)+ "}";
        res.type("application/json");
        return json;
    }

    public Integer updateDocumentAndgetCounter(String name) {
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.upsert(true);
        options.returnDocument(ReturnDocument.AFTER);
       
        Document doc = collection.findOneAndUpdate(eq("_id",name ), inc("counter", 1), options);
        return doc.getInteger("counter")*10+x;
    }

}

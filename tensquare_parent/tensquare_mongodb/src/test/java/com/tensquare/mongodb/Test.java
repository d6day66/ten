package com.tensquare.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Test {
    @org.junit.Test
    public void test(){
        MongoClient mongoClient = new MongoClient("192.168.200.130");
        MongoDatabase commentdb = mongoClient.getDatabase("commentdb");
        MongoCollection<Document> comment = commentdb.getCollection("comment");
        FindIterable<Document> documents = comment.find();
        for (Document document : documents) {

        }
        mongoClient.close();
    }
}

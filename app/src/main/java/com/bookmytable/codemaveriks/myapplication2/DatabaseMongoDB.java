//package com.bookmytable.codemaveriks.myapplication2;
//
//
//import android.util.Log;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoCollection;
//import org.bson.Document;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class DatabaseMongoDB {
//    public void connect() {
//
//        List<Document> seedData = new ArrayList<Document>();
//        seedData.add(new Document("decade", "1970s")
//                .append("artist", "Debby Boone")
//                .append("song", "You Light Up My Life")
//                .append("weeksAtOne", 10)
//        );
//        seedData.add(new Document("decade", "1980s")
//                        .append("artist", "Olivia Newton-John")
//                        .append("song", "Physical")
//                        .append("weeksAtOne", 10)
//        );
//
//        Log.d("tag4","here");
//        MongoClientURI uri = new MongoClientURI("mongodb://mratapattu1996:Manula1234@ds115729.mlab.com:15729/bookmytable");
//        Log.d("tag5","here");
//        MongoClient client = new MongoClient(uri);
//        Log.d("tag6","here");
//        MongoDatabase db = client.getDatabase(uri.getDatabase());
//        Log.d("tag7","here");
//
//        MongoCollection<Document> songs = db.getCollection("songs");
//        songs.insertMany(seedData);
//
//
//    }
//}

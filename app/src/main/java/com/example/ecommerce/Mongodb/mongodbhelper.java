package com.example.ecommerce.Mongodb;
import android.annotation.SuppressLint;
import android.util.Log;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class mongodbhelper {


        private static final String TAG = "MongoDBHelper";
        private MongoClient mongoClient;
        private MongoDatabase database;

        // Replace with your connection string
        @SuppressLint("AuthLeak")
        private static final String CONNECTION_STRING = "mongodb+srv://shwetasharma7984:IVuo1YGAREIdL47F@ecomm.a5jrr.mongodb.net/?retryWrites=true&w=majority&appName=ecomm";
        private static final String DATABASE_NAME = "ecomm"; //database name

        public mongodbhelper() {
            try {
                // Initialize connection
                ConnectionString connString = new ConnectionString(CONNECTION_STRING);
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connString)
                        .build();
                mongoClient = MongoClients.create(settings);

                // Select the database
                database = mongoClient.getDatabase(DATABASE_NAME);
                Log.d(TAG, "Connected to MongoDB database: " + DATABASE_NAME);
            } catch (Exception e) {
                Log.e(TAG, "Error connecting to MongoDB: " + e.getMessage());
            }
        }

        public void insertSampleData() {
            try {
                MongoCollection<Document> collection = database.getCollection("products");

                // Create a sample document
                Document sampleDoc = new Document("name", "Sample Product")
                        .append("price", 19.99)
                        .append("category", "Electronics");

                // Insert the document
                collection.insertOne(sampleDoc);
                Log.d(TAG, "Inserted sample document: " + sampleDoc.toJson());
            } catch (Exception e) {
                Log.e(TAG, "Error inserting document: " + e.getMessage());
            }
        }

        public void closeConnection() {
            if (mongoClient != null) {
                mongoClient.close();
                Log.d(TAG, "MongoDB connection closed.");
            }
        }
    }

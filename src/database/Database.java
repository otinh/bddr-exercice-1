package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> spells;

    public void create() {
        client = MongoClients.create();
        database = client.getDatabase("spellDB");
        spells = database.getCollection("spells");
    }

}

package database;

import com.mongodb.Block;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.List;

public class MongoDatabase {

    MongoCollection<Document> spells;

    private Block<Document> printBlock = document -> System.out.println(document.toJson());

    private MongoDatabase() {
        var client = MongoClients.create();
        var database = client.getDatabase("spellDB");
        spells = database.getCollection("spells");
    }

    private static class DatabaseHolder {
        private static final MongoDatabase INSTANCE = new MongoDatabase();
    }

    public static MongoDatabase getInstance() {
        return DatabaseHolder.INSTANCE;
    }

    public void clean() {
        spells.drop();
    }

    public void insert(Document document) {
        spells.insertOne(document);
    }

    public void insert(List<Document> documents) {
        documents.forEach(document -> {
            if (document != null)
                spells.insertOne(document);
        });
    }

    public void printAll() {
        spells.find().forEach(printBlock);
    }

}

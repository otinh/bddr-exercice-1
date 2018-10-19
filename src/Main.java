import crawler.Crawler;
import crawler.Spell;
import database.MongoDatabase;
import database.MapReducer;
import database.SQLiteDatabase;

import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
        var mongoDB = MongoDatabase.getInstance();
        var mapReducer = new MapReducer(mongoDB);
        var sqLiteDB = SQLiteDatabase.getInstance();

        var sc = new Scanner(System.in);
        int i;

        prompt();

        do {
            i = sc.nextInt();

            switch (i) {
                case 10:
                    mongoDB.clean();
                    System.out.println("MongoDatabase cleaned");
                    break;
                case 20:
                    System.out.println("Running...");
                    mongoDB.insert(Crawler.getSpells());
                    System.out.println("Spells inserted");
                    break;
                case 30:
                    System.out.println("MapReduce operating...");
                    mapReducer.mapReduce();
                    System.out.println("done");
                    break;
                case 40:
                    sqLiteDB.createTable();
                    break;
                case 50:
                    sqLiteDB.dropTable();
                    break;
                case 60:
                    System.out.println("Running...");
                    for (var j = 1; j < Crawler.getMaxId(); j++) {
                        System.out.println(j);
                        var spellDoc = Crawler.getSpell(j);
                        if (spellDoc == null) continue;
                        var spell = new Spell(spellDoc);
                        sqLiteDB.insert(spell);
                    }
                    System.out.println("Spells inserted");
                    break;
                case 99:
                    // Testing
                    var spellDoc = Crawler.getSpell(1841);
                    System.out.println(spellDoc);
                    var spell = new Spell(spellDoc);
                    sqLiteDB.insert(spell);
                    break;
                case 0:
                    exit(0);
            }

            prompt();

        } while (true);
    }

    private static void prompt() {
        System.out.println("Waiting for user's input...");
        System.out.println("-----");
        System.out.println("10 - (MongoDB) Clean database");
        System.out.println("20 - (MongoDB) Get every spells");
        System.out.println("30 - (MongoDB) Map Reduce");
        System.out.println("40 - (SQLite) Create table");
        System.out.println("50 - (SQLite) Drop table");
        System.out.println("60 - (SQLite) Insert every spells");
        System.out.println("0 - Exit");
        System.out.println("-----");
    }

}

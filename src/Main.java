import crawler.Crawler;
import database.Database;

public class Main {

    public static void main(String[] args) {
        Database db = new Database();
        db.create();
        db.insert(Crawler.getSpell(2));
        db.print();
    }

}

import crawler.Crawler;
import database.Database;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var db = Database.getInstance();
        var sc = new Scanner(System.in);
        int i;

        prompt();

        do
        {
            i = sc.nextInt();

            switch (i) {
                case 10:
                    db.clean();
                    System.out.println("Database cleaned");
                    break;
                case 20:
                    System.out.println("Running...");
                    db.insert(Crawler.getSpells());
                    System.out.println("Spells inserted");
                    break;
            }

            prompt();

        } while (i != 0);
    }

    private static void prompt() {
        System.out.println("Waiting for user's input...");
        System.out.println("-----");
        System.out.println("10 - Clean database");
        System.out.println("20 - Get every spells");
        System.out.println("0 - Exit");
        System.out.println("-----");
    }

}

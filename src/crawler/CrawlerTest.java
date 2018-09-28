package crawler;

import me.tongfei.progressbar.ProgressBar;
import org.bson.Document;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CrawlerTest {

    public static void main(String[] args) {
        printSpells(1);
    }

    private static void printSpell(int id) {
        System.out.println(Crawler.getSpell(id));
    }

    private static void printSpells(int number) {
        var progressBar = new ProgressBar("Running...", number);
        ArrayList<Document> list = new ArrayList<>();

        IntStream.rangeClosed(1, number).forEach(i -> {
            list.add(Crawler.getSpell(i));
            progressBar.step();
        });

        progressBar.close();
        list.forEach(System.out::println);
    }

    private static void printSpells(int from, int to) {
        IntStream.rangeClosed(from, to).forEach(CrawlerTest::printSpell);
    }

}

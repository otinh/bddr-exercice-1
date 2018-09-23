package crawler;

import com.google.gson.JsonObject;
import me.tongfei.progressbar.ProgressBar;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class CrawlerTest {

    public static void main(String[] args) {
        printSpell(193);
    }

    private static void printSpell(int id) {
        System.out.println(new Crawler().getSpell(id));
    }

    private static void printSpells(int number) {
        var progressBar = new ProgressBar("Running...", number);
        var list = new ArrayList<JsonObject>();

        IntStream.rangeClosed(1, number).forEach(i -> {
            list.add(new Crawler().getSpell(i));
            progressBar.step();
        });

        progressBar.close();
        list.forEach(System.out::println);
    }

    private static void printSpells(int from, int to) {
        IntStream.rangeClosed(from, to).forEach(CrawlerTest::printSpell);
    }

}

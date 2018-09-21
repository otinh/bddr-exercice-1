package crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

class Crawler {

    private final String URL = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=";
    private final int MAX_ID = 1975;

    private Document htmlDocument;

    private String url(int id) {
        return URL + id;
    }

    void searchSpell(int id) {
        connectTo(url(id));
        HtmlParser parser = new HtmlParser(htmlDocument);
        var spell = parser.parseToJson();

        System.out.println(spell);
    }

    void searchSpells() {
        searchSpells(MAX_ID);
    }

    void searchSpells(int limit) {
        for (var id = 1; id <= limit; id++) {
            connectTo(url(id));
            HtmlParser parser = new HtmlParser(htmlDocument);
            var spell = parser.parseToJson();

            System.out.println(spell);
        }
    }

    private void connectTo(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            htmlDocument = connection.get();
        } catch (IOException e) {
            System.err.println("Error in HTTP request: " + e);
        }
    }

}

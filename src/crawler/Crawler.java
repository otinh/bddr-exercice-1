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
    }

    void searchSpells() {
        searchSpells(MAX_ID);
    }

    private void searchSpells(int limit) {
        for (var id = 1; id <= limit; id++) {
            connectTo(url(id));
            HtmlParser parser = new HtmlParser(htmlDocument);
            var spell = parser.parseToJson();
        }
    }

    /*
    * Enregistre la page web dans un Document qui sera traité après.
    * @param url lien vers le site concerné
    * */
    private void connectTo(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            htmlDocument = connection.get();
        } catch (IOException e) {
            System.err.println("Error in HTTP request: " + e);
        }
    }

}

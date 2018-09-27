package crawler;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crawler {

    private static final String URL = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=";
    private static final int MAX_ID = 1975;

    private static String url(int id) {
        return URL + id;
    }

    /*
    * Retourne un sort en particulier.
    * */
    public static JsonObject getSpell(int id) {
        return getSpellInfo(id);
    }

    /*
    * Retourne tous les sorts existants.
    * */
    public static List<JsonObject> getSpells() {
        return getSpells(MAX_ID);
    }

    /*
    * Retourne les X premiers sorts existants.
    * @param size = X
    * */
    public static List<JsonObject> getSpells(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(Crawler::getSpellInfo)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /*
    * Récupère les informations de la page HTML concernant le sort, puis le transforme
    * en JSON.
    * @return le sort en JSON
    * */
    private static JsonObject getSpellInfo(int id) {
        try {
            var connection = Jsoup.connect(url(id));
            return new SpellParser(connection.get()).parseToJson();
        } catch (IOException e) {
            System.err.println("Error in HTTP request: " + e);
        }
        return null;
    }

}
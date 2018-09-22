package crawler;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Crawler {

    private final String URL = "http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=";
    private final int MAX_ID = 1975;

    private String url(int id) {
        return URL + id;
    }

    /*
    * Retourne un sort en particulier.
    * */
    JsonObject getSpell(int id) {
        return Objects.requireNonNull(getSpellInfo(id));
    }

    /*
    * Retourne tous les sorts existants.
    * */
    List<JsonObject> getSpells() {
        return getSpells(MAX_ID);
    }

    /*
    * Retourne les X premiers sorts existants.
    * @param size = X
    * */
    List<JsonObject> getSpells(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(id -> Objects.requireNonNull(getSpellInfo(id)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /*
    * @return les informations d'un sort à l'aide de son ID.
    * */
    private JsonObject getSpellInfo(int id) {
        try {
            var connection = Jsoup.connect(url(id));
            return new SpellParser(connection.get()).parseToJson();
        } catch (IOException e) {
            System.err.println("Error in HTTP request: " + e);
        }
        System.err.println("Could not get spell info for ID=" + id);
        return null;
    }

}
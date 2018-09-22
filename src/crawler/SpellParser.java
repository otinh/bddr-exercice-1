package crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SpellParser {

    private JsonObject spell;

    private Elements nameElements;
    private Elements detElements;
    private Elements descElements;

    SpellParser(Document document) {
        this.nameElements = document.getElementsByClass("heading");
        this.detElements = document.getElementsByClass("SPDet");
        this.descElements = document.getElementsByClass("SPDesc");
    }

    /*
    * Parse toutes les informations de la page HTML d'un sort.
    * @return le sort dans un format JSON.
    * */
    JsonObject parseToJson() {
        spell = new JsonObject();

        // Main info
        parseName();
        parseSchool();
        parseClassAndLevel();
        // Casting
        parseCastingTime();
        parseComponents();
        // Effect
        parseSpellResistance();
        // Description
        parseDescription();

        return spell;
    }

    private void parseName() {
        spell.addProperty("name", nameElements.first().text());
    }

    private void parseSchool() {
        var m = process(detElements, "(?<=School ).*(?=;)");
        if (m.find()) spell.addProperty("school", m.group());
    }

    private void parseClassAndLevel() {
        parseClassAndLevel("(?<=sorcerer\\/wizard )\\d", true);
    }

    /*
    * Note: si le sort n'est pas de classe "wizard", on prend la première classe (et niveau)
    * parmi celles proposées dans la page.
    * */
    private void parseClassAndLevel(String regex, boolean isWizardSpell) {
        var m = process(detElements, regex);

        if (m.find()) {
            if (isWizardSpell) {
                spell.addProperty("class", "wizard");
                spell.addProperty("level", m.group());
            } else {
                spell.addProperty("class", m.group(1));
                spell.addProperty("level", m.group(2));
            }
        } else {
            assert isWizardSpell : "Could not find spell class or level";
            parseClassAndLevel("(\\w+) (\\d{1})", false);
        }
    }

    private void parseCastingTime() {
        var m = process(detElements.get(1), "(?<=Casting Time ).*");
        if (m.find()) spell.addProperty("casting_time", m.group());
    }

    /*
    * Note: on ne prend pas en compte les composants "M/DF" et "F/DF"
    * */
    private void parseComponents() {
        var components = new JsonArray();
        var letters = new String[]{"V", "S", "M", "F", "XP"};

        Arrays.stream(letters)
                .filter(detElements.get(2).text()::contains)
                .forEach(components::add);
        spell.add("components", components);
    }

    private void parseSpellResistance() {
        for (var element : detElements) {
            if (!element.text().contains("Spell Resistance")) continue;

            var m = process(element, "(?<=Resistance )[yesno]+");
            if (m.find()) spell.addProperty("spell_resistance", m.group().equals("yes"));
            return;
        }

        spell.addProperty("spell_resistance", false);
    }

    private void parseDescription() {
        spell.addProperty("description", descElements.get(0).text());
    }

    /*
    * Applique une expression régulière à un Element.
    * */
    private Matcher process(Element element, String regex) {
        return Pattern.compile(regex).matcher(element.text());
    }

    /*
     * Applique une expression régulière à un Elements.
     * */
    private Matcher process(Elements elements, String regex) {
        return Pattern.compile(regex).matcher(elements.first().text());
    }

}

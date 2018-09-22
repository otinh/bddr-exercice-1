package crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HtmlParser {

    private Document document;
    private JsonObject spell;

    private Elements nameElements;
    private Elements detElements;
    private Elements descElements;

    HtmlParser(Document document) {
        this.document = document;
        this.nameElements = document.getElementsByClass("heading");
        this.detElements = document.getElementsByClass("SPDet");
        this.descElements = document.getElementsByClass("SPDesc");
    }

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
        var m = process("(?<=School ).*(?=;)");
        if (m.find()) spell.addProperty("school", m.group());
    }

    private void parseClassAndLevel() {
        parseClassAndLevel("(?<=sorcerer\\/wizard )\\d", true);
    }

    private void parseClassAndLevel(String regex, boolean isWizardSpell) {
        var m = process(regex);

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
        var m = process("(?<=Casting Time ).*", detElements.get(1));
        if (m.find()) spell.addProperty("casting_time", m.group());
    }

    private void parseComponents() {
        var components = new JsonArray();
        var letters = new String[]{"V", "S", "M", "F", "XP"};

        String text = detElements.get(2).text();
        Arrays.stream(letters).filter(text::contains).forEach(components::add);
        spell.add("components", components);
    }

    private void parseSpellResistance() {
        for (var element : detElements) {
            if (!element.text().contains("Spell Resistance")) continue;

            var m = process("(?<=Resistance )[yesno]+", element);
            if (m.find()) spell.addProperty("spell_resistance", m.group().equals("yes"));
            return;
        }

        spell.addProperty("spell_resistance", false);
    }

    private void parseDescription() {
        spell.addProperty("description", descElements.get(0).text());
    }

    private Matcher process(String regex) {
        var p = Pattern.compile(regex);
        return p.matcher(detElements.first().text());
    }

    private Matcher process(String regex, Element element) {
        var p = Pattern.compile(regex);
        return p.matcher(element.text());
    }

}

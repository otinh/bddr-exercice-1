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
        if (isInvalidHtml()) return null;

        spell = new JsonObject();

        // Main info
        parseName();
        parseSchool();
        parseClassAndLevel();

        // Casting
        parseCastingTime();
        parseComponents();

        // Effect
        parseRange();
        parseArea();
        parseEffect();
        parseTargets();
        parseDuration();
        parseSavingThrow();
        parseSpellResistance();

        // Description
        parseDescription();

        // Domain
        parseDomain();

        return spell;
    }

    private boolean isInvalidHtml() {
        return (nameElements.toString().isEmpty() ||
                detElements.toString().isEmpty() ||
                descElements.toString().isEmpty());
    }

    private void parseName() {
        spell.addProperty("name", nameElements.first().text());
    }

    private void parseSchool() {
        var m = process(detElements.get(0), "(?<=School ).*(?=;)");
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
        var m = process(detElements.get(0), regex);

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

    private void parseRange() {
        var m = process(detElements.get(3), "(?<=Range ).*");
        if (m != null && m.find()) spell.addProperty("range", m.group());
    }

    private void parseArea() {
        var m = process(findElement("Area", detElements), "(?<=Area ).*");
        if (m != null && m.find()) spell.addProperty("area", m.group());
    }

    private void parseEffect() {
        var m = process(findElement("Effect", detElements), "(?<=Effect ).*");
        if (m != null && m.find()) spell.addProperty("effect", m.group());
    }

    private void parseTargets() {
        var m = process(findElement("Targets", detElements), "(?<=Targets ).*");
        if (m != null && m.find()) spell.addProperty("targets", m.group());
    }

    private void parseDuration() {
        var m = process(findElement("Duration", detElements), "(?<=Duration ).*");
        if (m != null && m.find()) spell.addProperty("duration", m.group());
    }

    private void parseSavingThrow() {
        var m = process(findElement("Saving Throw", detElements), "(?<=Saving Throw )[a-zA-Z ]+(?=;)");
        if (m != null && m.find()) spell.addProperty("saving_throw", m.group());
    }

    private void parseSpellResistance() {
        var m = process(findElement("Spell Resistance", detElements), "(?<=Resistance )[yesno]+");
        if (m != null && m.find())
            spell.addProperty("spell_resistance", m.group().equals("yes"));
        else
            spell.addProperty("spell_resistance", false);
    }

    private void parseDescription() {
        spell.addProperty("description", descElements.get(0).text());
    }

    private void parseDomain() {
        if (descElements.size() < 2) return;
        spell.addProperty("domain", descElements.get(1).text());
    }

    /*
    * Recherche un Element en fonction de son nom dans une collection d'Elements.
    * @param name nom de l'Element recherché
    * @param elements collection d'Elements où on effectue la recherche
    * */
    private Element findElement(String name, Elements elements) {
        return elements.stream()
                .filter(element -> element.text().contains(name))
                .findFirst()
                .orElse(null);
    }

    /*
    * Applique une expression régulière à un Element.
    * */
    private Matcher process(Element element, String regex) {
        if (element == null) return null;
        return Pattern.compile(regex).matcher(element.text());
    }

}

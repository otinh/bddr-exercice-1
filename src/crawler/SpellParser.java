package crawler;

import org.bson.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SpellParser {

    private org.bson.Document spell;

    private Elements nameElements;
    private Elements detElements;
    private Elements descElements;

    SpellParser(org.jsoup.nodes.Document document) {
        this.nameElements = document.getElementsByClass("heading");
        this.detElements = document.getElementsByClass("SPDet");
        this.descElements = document.getElementsByClass("SPDesc");
    }

    /*
    * Parse toutes les informations de la page HTML d'un sort.
    * @return le sort dans un format JSON.
    * */
    Document parseToJson() {
        if (isInvalidHtml()) return null;

        spell = new org.bson.Document();

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
        spell.append("name", nameElements.first().text());
    }

    private void parseSchool() {
        var m = process(detElements.get(0), "(?<=School ).*(?=;)");
        if (m.find()) spell.append("school", m.group());
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
                spell.append("class", "wizard");
                spell.append("level", m.group());
            } else {
                spell.append("class", m.group(1));
                spell.append("level", m.group(2));
            }
        } else {
            assert isWizardSpell : "Could not find spell class or level";
            parseClassAndLevel("(\\w+) (\\d{1})", false);
        }
    }

    private void parseCastingTime() {
        var m = process(detElements.get(1), "(?<=Casting Time ).*");
        if (m.find()) spell.append("casting_time", m.group());
    }

    /*
    * Note: on ne prend pas en compte les composants "M/DF" et "F/DF"
    * */
    private void parseComponents() {
        var letters = new ArrayList<String>();

        if (detElements.get(2).text().contains("V"))
            letters.add("V");
        if (detElements.get(2).text().contains("S"))
            letters.add("S");
        if (detElements.get(2).text().contains("M"))
            letters.add("M");
        if (detElements.get(2).text().contains("F"))
            letters.add("F");
        if (detElements.get(2).text().contains("XP"))
            letters.add("XP");

        spell.append("components", letters);
    }

    private void parseRange() {
        var m = process(detElements.get(3), "(?<=Range ).*");
        if (m != null && m.find()) spell.append("range", m.group());
    }

    private void parseArea() {
        var m = process(findElement("Area", detElements), "(?<=Area ).*");
        if (m != null && m.find()) spell.append("area", m.group());
    }

    private void parseEffect() {
        var m = process(findElement("Effect", detElements), "(?<=Effect ).*");
        if (m != null && m.find()) spell.append("effect", m.group());
    }

    private void parseTargets() {
        var m = process(findElement("Targets", detElements), "(?<=Targets ).*");
        if (m != null && m.find()) spell.append("targets", m.group());
    }

    private void parseDuration() {
        var m = process(findElement("Duration", detElements), "(?<=Duration ).*");
        if (m != null && m.find()) spell.append("duration", m.group());
    }

    private void parseSavingThrow() {
        var m = process(findElement("Saving Throw", detElements), "(?<=Saving Throw )[a-zA-Z(), ]+(?=;)");
        if (m != null && m.find()) spell.append("saving_throw", m.group());
    }

    private void parseSpellResistance() {
        var m = process(findElement("Spell Resistance", detElements), "(?<=Resistance )[yesno]+");
        if (m != null && m.find())
            spell.append("spell_resistance", m.group().equals("yes"));
        else
            spell.append("spell_resistance", false);
    }

    private void parseDescription() {
        spell.append("description", descElements.get(0).text());
    }

    private void parseDomain() {
        if (descElements.size() < 2) return;
        spell.append("domain", descElements.get(1).text());
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

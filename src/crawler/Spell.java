package crawler;

import org.bson.Document;

public class Spell {

    private static Document spellDoc;

    public String getName() {
        return name;
    }

    public String getSchool() {
        return school;
    }

    public String getSpell_class() {
        return spell_class;
    }

    public int getLevel() {
        return level;
    }

    public String getCasting_time() {
        return casting_time;
    }

    public String getComponents() {
        return components;
    }

    public String getRange() {
        return range;
    }

    public String getEffect() {
        return effect;
    }

    public String getDuration() {
        return duration;
    }

    public String getSaving_throw() {
        return saving_throw;
    }

    public boolean isSpell_resistance() {
        return spell_resistance;
    }

    public String getDescription() {
        return description;
    }

    public String getTargets() {
        return targets;
    }

    public String getDomain() {
        return domain;
    }

    public String getArea() {
        return area;
    }

    private String name;
    private String school;
    private String spell_class;
    private int level;
    private String casting_time;
    private String components;
    private String range;
    private String effect;
    private String duration;
    private String saving_throw;
    private boolean spell_resistance;
    private String description;
    private String targets;
    private String domain;
    private String area;

    public Spell(Document spellDoc) {
        Spell.spellDoc = spellDoc;
        fillInfo();
    }

    private void fillInfo() {
        this.name = setAsString("name");
        this.school = setAsString("school");
        this.spell_class = setAsString("class");
        this.level = setAsInteger("level");
        this.casting_time = setAsString("casting_time");
        this.components = setAsString("components");
        this.range = setAsString("range");
        this.effect = setAsString("effect");
        this.duration = setAsString("duration");
        this.saving_throw = setAsString("saving_throw");
        this.spell_resistance = setAsBoolean("spell_resistance");
        this.description = setAsString("description");
        this.targets = setAsString("targets");
        this.domain = setAsString("domain");
        this.area = setAsString("area");
    }

    private static String setAsString(String query) {
        var info = spellDoc.get(query);
        return info != null ? info.toString() : "";
    }

    private static int setAsInteger(String query) {
        var info = spellDoc.get(query);
        return info != null ? Integer.parseInt(info.toString()) : -1;
    }

    private static boolean setAsBoolean(String query) {
        var info = spellDoc.get(query);
        return info != null && Boolean.parseBoolean(info.toString());
    }

}

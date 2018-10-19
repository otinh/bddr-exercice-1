package database;

import crawler.Spell;

import java.sql.*;

public class SQLiteDatabase {

    private final String URL = "jdbc:sqlite:D:/Cours/SQLite/db/spellsDB.db";

    private SQLiteDatabase() {
    }

    private static class DatabaseHolder {
        private static final SQLiteDatabase INSTANCE = new SQLiteDatabase();
    }

    public static SQLiteDatabase getInstance() {
        return DatabaseHolder.INSTANCE;
    }

    public void createNewDatabase() {
        try (Connection connection = DriverManager.getConnection(URL)) {
            if (connection == null) return;

            var meta = connection.getMetaData();
            System.out.println("Driver name is " + meta.getDriverName());
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void createTable() {
        var query =
                "CREATE TABLE IF NOT EXISTS spells (\n" +
                        "  id               integer PRIMARY KEY,\n" +
                        "  name             text NOT NULL,\n" +
                        "  school           text,\n" +
                        "  class            text,\n" +
                        "  level            integer,\n" +
                        "  casting_time     text,\n" +
                        "  components       text,\n" +
                        "  range            text,\n" +
                        "  effect           text,\n" +
                        "  duration         text,\n" +
                        "  saving_throw     text,\n" +
                        "  spell_resistance integer,\n" +
                        "  description      text,\n" +
                        "  targets          text,\n" +
                        "  domain           text,\n" +
                        "  area             text\n" +
                        ")";

        try (Connection connection = DriverManager.getConnection(URL)) {
            var statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Spell spell) {
        var name = spell.getName();
        var school = spell.getSchool();
        var spellClass = spell.getSpell_class();
        var level = spell.getLevel();
        var castingTime = spell.getCasting_time();
        var components = spell.getComponents();
        var range = spell.getRange();
        var effect = spell.getEffect();
        var duration = spell.getDuration();
        var savingThrow = spell.getSaving_throw();
        var spellResistance = spell.isSpell_resistance();
        var description = spell.getDescription();
        var targets = spell.getTargets();
        var domain = spell.getDomain();
        var area = spell.getArea();
        var query = "INSERT INTO spells\n" +
                "    (name, school, class, level, casting_time, components, range, effect,\n" +
                "        duration, saving_throw, spell_resistance, description, targets, domain, area)\n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = this.connect()) {
            var statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, school);
            statement.setString(3, spellClass);
            statement.setInt(4, level);
            statement.setString(5, castingTime);
            statement.setString(6, components);
            statement.setString(7, range);
            statement.setString(8, effect);
            statement.setString(9, duration);
            statement.setString(10, savingThrow);
            statement.setBoolean(11, spellResistance);
            statement.setString(12, description);
            statement.setString(13, targets);
            statement.setString(14, domain);
            statement.setString(15, area);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS spells;";

        try (Connection connection = this.connect()) {
            var statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

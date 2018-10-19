package database;

public class MapReducer {

    private MongoDatabase database;

    public MapReducer(MongoDatabase database) {
        this.database = database;
    }

    public void mapReduce() {
        String map = "function () {\n" +
                "if (this.class === 'wizard' &&\n" +
                "    this.level <= 4 &&\n" +
                "    this.components.length === 1 &&\n" +
                "    this.components[0] === 'V') {\n" +
                "emit(this.name, this.components[0]);\n" +
                //"emit(this.name, this.level);\n" +
                "}};";
        String reduce = "function(keyName, valueClass) {\n" +
                "    return Array.sum(valueClass);\n" +
                "}";
        var result = database.spells.mapReduce(map, reduce);
        for (var e : result) System.out.println(e);
    }

}
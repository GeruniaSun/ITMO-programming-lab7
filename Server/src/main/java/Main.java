import lt.shgg.database.DatabaseParser;
import lt.shgg.app.Receiver;
import lt.shgg.app.Storage;

import java.util.LinkedHashSet;

public class Main {
    public static void main(String[] args) {

        var databaseParser = new DatabaseParser();
        var storage = new Storage(new LinkedHashSet<>(databaseParser.load()));
        var receiver = new Receiver(storage);
        var server = new Server(1488, receiver);
        server.run();
    }
}

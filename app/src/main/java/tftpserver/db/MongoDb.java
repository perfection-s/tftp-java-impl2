package tftpserver.db;

import java.util.List;

public class MongoDb implements Db {
    // Variables:
    // MongoDriver driver;
    // MongoConnectionInstance conn;
    private static MongoDb mongo;

    private MongoDb() {
        // Load the driver
        // Class.forName(//MongoDriver.class)

        // Establish connection (driver.Connect(MongoURL))
        // mongoConnection = driver.Connect(mongo://localhost:3353)
    }

    public static MongoDb getDb() {
        return mongo;
    }

    @Override
    public void saveFile(String filename, List<String> content) {
        // Mongo Save Code
    }

    @Override
    public List<String> getFile(String filename) {
        // Mongo Get Code
        return null;
    }
}

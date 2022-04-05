package tftpserver.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * InMemDb holds filenames and the file contents in a map that is thread safe.
 *
 * The map has the keys as the filenames and the values as a list of strings. Which looks
 * something like this:
 *
 * Filename1 <-> < {block 0 data}, {block 1 data}, ... , {block `n` data}>
 * Filename2 <-> < {block 0 data}, {block 1 data}, ... , {block `n` data}>
 * Filename3 <-> < {block 0 data}, {block 1 data}, ... , {block `n` data}>
 */

public class InMemDb implements Db {

    // TODO (For Vidit): Can you change this to use a sync. map?
    private Map<String, List<byte[]>> inMemDb = null;
    private static InMemDb db;
    private Lock lock;

    public static InMemDb getDb() {
        if (db == null) {
            // Control would come here typically (And ONLY)
            // the first time in the lifetime of the server.
            db = new InMemDb();
        }
        return db;
    }

    private InMemDb() {
        inMemDb = new HashMap<String, List<byte[]>>();
    }

    @Override
    public void saveFile(String filename, List<byte[]> content) {
        lock.lock();
        try {
            inMemDb.put(filename, content);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<byte[]> getFile(String filename) {
        lock.lock();
        try {
            return inMemDb.get(filename);
        } finally {
            lock.unlock();
        }
    }
}

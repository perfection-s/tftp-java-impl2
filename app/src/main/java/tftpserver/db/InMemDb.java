package tftpserver.db;

import tftpserver.core.TFTPDataChunk;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    // TODO (For Vidit): Can you change this to use a ConcurrentHashMap?
    private final Map<String, PriorityQueue<TFTPDataChunk>> inMemDb;
    private static InMemDb db;
    private final Lock lock;

    public static InMemDb getDb() {
        if (db == null) {
            // Control would come here typically (And ONLY)
            // the first time in the lifetime of the server.
            db = new InMemDb();
        }
        return db;
    }

    private InMemDb() {
        inMemDb = new HashMap<>();
        lock = new ReentrantLock();
    }

    @Override
    public void saveFile(String filename, List<TFTPDataChunk> content) {
        PriorityQueue<TFTPDataChunk> pq = new PriorityQueue<>();
        pq.addAll(content);

        lock.lock();
        try {
            inMemDb.put(filename, pq);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<byte[]> getFile(String filename) {
        List<byte[]> fileContents = new ArrayList<>();
        lock.lock();
        try {
            PriorityQueue<TFTPDataChunk> pq = inMemDb.get(filename);

            while (!pq.isEmpty()) {
                fileContents.add(pq.poll().getData());
            }
            return fileContents;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public boolean isPresent(String filename) {
        if(inMemDb.containsKey(filename)) {return true;}
        return false;
    }
    
}

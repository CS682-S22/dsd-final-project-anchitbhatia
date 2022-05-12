package client;

import models.Torrent;
import utils.FileIO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Alberto Delgado on 5/11/22
 * @project bittorrent
 * <p>
 * Same information as torrent file, but additionally it has information
 * on what pieces it has downloaded
 */
public class Library {
    Map<String, File> files = new HashMap<>();
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Library() {
    }

    public File add(Torrent torrent) {
        File file = new File(torrent);
        lock.writeLock().lock();
        try {
            files.put(file.name, file);
            return file;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(String filename) {
        lock.writeLock().lock();
        try {
            files.remove(filename);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void remove(Torrent torrent) {
        lock.writeLock().lock();
        try {
            files.remove(torrent.name);
        } finally {
            lock.writeLock().unlock();
        }
    }

    static class File extends Torrent {
        public final boolean[] downloadedPieces;

        public File(Torrent t) {
            super(t.announce,
                    t.name,
                    t.pieceLength,
                    t.pieces,
                    t.singleFileTorrent,
                    t.totalSize,
                    t.fileList,
                    t.comment,
                    t.createdBy,
                    t.creationDate,
                    t.announceList,
                    t.infoHash);

            boolean[] p;
            try {
                p = checkDownloadedPieces();
            } catch (IOException e) {
                // we don't have the file or corrupted or something
                p = null;
            }
            downloadedPieces = p;
        }

        private boolean[] checkDownloadedPieces() throws IOException {
            byte[] data = FileIO.getInstance().readFile(name);
            int numberOfPieces = (int) (totalSize / pieceLength);
            if (totalSize % pieceLength != 0)
                numberOfPieces++;

            boolean[] downloadedPieces = new boolean[numberOfPieces];
            int j = 0;
            for (int i = 0; i < totalSize; i += pieceLength) {
                if (data[i] != 0) downloadedPieces[j++] = true;
            }

            return downloadedPieces;
        }
    }
}

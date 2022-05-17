package models;

import utils.FileIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alberto Delgado on 5/9/22
 * @project dsd-final-project-anchitbhatia
 * <p>
 * Basic torrent data structure.
 * <p>
 * Used this as a template: <a href="https://github.com/m1dnight/torrent-parser/blob/master/src/main/java/be/christophedetroyer/torrent/Torrent.java">https://github.com/m1dnight/torrent-parser/blob/master/src/main/java/be/christophedetroyer/torrent/Torrent.java</a>
 */
public class Torrent {
    public final String announce;               // tracker announcer
    public final String name;                   // file name (not torrent name)
    public final Long pieceLength;              // bytes per piece
    public final List<String> pieces;           // SHA1 bytes of each piece
    public final boolean singleFileTorrent;     // is single/multi file
    public final Long totalSize;                // total size in bytes
    public final List<TorrentFile> fileList;    // list of files (if multi file torrent)
    public final String comment;                // optional comment about file
    public final String createdBy;              // uploader "author"
    public final Date creationDate;             // creation date
    public final List<String> announceList;     // list of announcers/trackers
    public final String infoHash;               // SHA256 of entire file (not torrent)
    public final List<Long> downloadedPieces = new ArrayList<>(); // Local "owned" pieces

    public Torrent(
            String announce,
            String name,
            Long pieceLength,
            List<String> pieces,
            boolean singleFileTorrent,
            Long totalSize,
            List<TorrentFile> fileList,
            String comment,
            String createdBy,
            Date creationDate,
            List<String> announceList,
            String infoHash
    ) {
        this.announce = announce;
        this.name = name;
        this.pieceLength = pieceLength;
        this.pieces = pieces;
        this.singleFileTorrent = singleFileTorrent;
        this.totalSize = totalSize;
        this.fileList = fileList;
        this.comment = comment;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.announceList = announceList;
        this.infoHash = infoHash;
    }

    public class TorrentFile {
        public final Long fileLength;
        public final List<String> fileDirs;

        public TorrentFile(Long fileLength, List<String> fileDirs) {
            this.fileLength = fileLength;
            this.fileDirs = fileDirs;
        }
    }

    public String getName() {
        int i = name.indexOf('.');
        return name.substring(0, i) + ".torrent";
    }

    public void checkDownloadedPieces() throws IOException {
        byte[] data = FileIO.getInstance().readFile(name);
        int numberOfPieces = (int) (totalSize / pieceLength);
        if (totalSize % pieceLength != 0)
            numberOfPieces++;

        List<Long> downloadedPieces = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < totalSize; i += pieceLength) {
            if (data[i] != 0) downloadedPieces.add((long) (i + 1));
        }
    }

    @Override
    public String toString() {
        return "Torrent{" + '\n' +
                "    announce=" + announce + '\n' +
                "    name=" + name + '\n' +
                "    pieceLength=" + pieceLength + '\n' +
                "    pieces=" + pieces + '\n' +
                "    singleFileTorrent=" + singleFileTorrent + '\n' +
                "    totalSize=" + totalSize + '\n' +
                "    fileList=" + fileList + '\n' +
                "    comment=" + comment + '\n' +
                "    createdBy='" + createdBy + '\n' +
                "    creationDate=" + creationDate + '\n' +
                "    announceList=" + announceList + '\n' +
                "    infoHash='" + infoHash + '\n' +
                '}';
    }
}

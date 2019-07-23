package pl.edu.mimuw.mw406415.Indexer.Crawler;

import org.apache.tika.Tika;
import pl.edu.mimuw.mw406415.IndexManager.LangIndexManager;
import pl.edu.mimuw.mw406415.Indexer.LangDetector.LangDetector;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Crawler {
    public void editIndex(final String stringpath, OperationMode mode) {
        try (LangIndexManager englishIndex = new LangIndexManager("en"); LangIndexManager polishIndex = new LangIndexManager("pl")) {
            File f = new File(stringpath);
            if (Files.isRegularFile(f.toPath())) {
                Tika tika = new Tika();
                try (InputStream is = new FileInputStream(f); BufferedInputStream bis = new BufferedInputStream(is)) {
                    String fileContents = tika.parseToString(is);
                    String fileLang = LangDetector.detect(fileContents);
                    LangIndexManager currentIndex = englishIndex;
                    if (fileLang.equals("en")) {
                        currentIndex = polishIndex;
                    }
                    if (mode == OperationMode.AddToIndex) {
                        currentIndex.indexFileByValues(f.getCanonicalPath(), fileContents);
                    } else {
                        currentIndex.deleteByFilepath(f.getCanonicalPath());
                    }
                } catch (Exception ignore) {}
            }
        }
    }
    public void recursiveCrawl(final String stringpath, OperationMode mode) {
        File f = new File(stringpath);
        try {
            Files.walkFileTree(f.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs) {
                    editIndex(dir.toString(), mode);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Cannot handle filepath");
        }
    }
}

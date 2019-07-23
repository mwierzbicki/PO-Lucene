package pl.edu.mimuw.mw406415.IndexManager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public abstract class AIndexManager implements AutoCloseable {
    protected String indexPath = System.getProperty("user.home") + "/.index";
    protected Analyzer analyzer;
    protected IndexReader reader;
    protected IndexWriter writer;
    protected IndexSearcher searcher;
    protected Boolean readingInit = false;
    protected Boolean searchingInit = false;

    protected void initializeRead() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(dir, iwc);
            readingInit = true;
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
    protected void initializeSearch() {
        try {
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            reader = DirectoryReader.open(dir);
            searcher = new IndexSearcher(reader);
            searchingInit = true;
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
    protected void addToIndex(Document d) {
        if (!readingInit) {
            initializeRead();
        }
        try {
            writer.updateDocument(new Term("path", d.get("path")), d);
            writer.commit();
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
    public void deleteByFilepath(String filepath) {
        if (!readingInit) {
            initializeRead();
        }
        try {
            writer.deleteDocuments(new Term("path",filepath));
            writer.commit();
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
    public void clear() {
        try {
            writer.deleteAll();
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
    @Override
    public void close() {
        if (readingInit)
        try {
            writer.close();
        }
        catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
    }
}

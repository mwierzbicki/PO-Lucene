package pl.edu.mimuw.mw406415.IndexManager;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;

import java.io.File;

public class MonitorIndexManager extends AIndexManager {
    public MonitorIndexManager() {
        indexPath += "/monitor";
        analyzer = new StandardAnalyzer();
    }
    public void addFilepath(String filepath) {
        Document doc = new Document();
        File f = new File(filepath);
        try {
            doc.add(new StringField("path", f.getCanonicalPath(), Field.Store.YES));
            addToIndex(doc);
        } catch (Exception e) {
            System.err.println("ERROR: Cannot resolve canonical path");
        }
    }
    public String[] getAllPaths() {
        String[] retval = null;
        try {
            TopDocs t = searcher.search(new MatchAllDocsQuery(), Integer.MAX_VALUE);
            retval = new String[t.scoreDocs.length];
            for (int i=0; i<t.scoreDocs.length; i++)
            {
                Document doc = searcher.doc(t.scoreDocs[i].doc);
                retval[i] = doc.get("path");
            }
        } catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
        return retval;
    }
}

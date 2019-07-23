package pl.edu.mimuw.mw406415.IndexManager;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.tika.langdetect.OptimaizeLangDetector;

public class LangIndexManager extends AIndexManager {
    private OptimaizeLangDetector detector;
    public LangIndexManager(String lang) {
        indexPath += ("/" + lang);
        if (lang.equals("pl")) {
            analyzer = new PolishAnalyzer();
        }
        else if (lang.equals("en")) {
            analyzer = new EnglishAnalyzer();
        }

    }
    public void indexFileByValues(String filepath, String parsedContents) {
        Document doc = new Document();
        doc.add(new StringField("path", filepath, Field.Store.YES));
        doc.add(new TextField("contents", parsedContents, Field.Store.YES));
        addToIndex(doc);
    }
    public SearchResults searchInIndex(Query query, Integer howManyResults) {
        if (!searchingInit) {
            initializeSearch();
        }
        SearchResults retval = new SearchResults();
        try {
            TopDocs docs = searcher.search(query, howManyResults);
            UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
            retval.highlighted = highlighter.highlight("contents", query, docs, 5);
            ScoreDoc[] hits = docs.scoreDocs;
            retval.totalHits = hits.length;
            retval.filepaths = new String[retval.totalHits];
            for (int i=0; i<retval.totalHits; i++)
            {
                Document docc = searcher.doc(hits[i].doc);
                retval.filepaths[i] = docc.get("path");
            }
        } catch (Exception e) { System.err.println("FATAL: Cannot access index"); System.exit(1); }
        return retval;
    }
}

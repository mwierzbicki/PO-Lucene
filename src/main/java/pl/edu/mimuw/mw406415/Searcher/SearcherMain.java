package pl.edu.mimuw.mw406415.Searcher;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import pl.edu.mimuw.mw406415.IndexManager.LangIndexManager;
import pl.edu.mimuw.mw406415.IndexManager.SearchResults;

import java.io.IOException;

public class SearcherMain {
    public static void main(String[] args) {
        LangIndexManager englishIndex = new LangIndexManager("en");
        LangIndexManager polishIndex = new LangIndexManager("pl");
        LangIndexManager currentIndex = englishIndex;
        Boolean showDetails = false;
        Integer resultsLimit = Integer.MAX_VALUE;
        Boolean showColor = true;
        QueryTypes queryMode = QueryTypes.Term;
        try (Terminal terminal = TerminalBuilder.builder().jna(false).jansi(true).build()) {
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            while (true) {
                String line = null;
                try {
                    line = lineReader.readLine("> ");
                    String[] tok = line.split(" ");
                    if (tok[0].equals("%lang")) {
                        if (tok[1].equals("en")) currentIndex = englishIndex;
                        else if (tok[1].equals("pl")) currentIndex = polishIndex;
                    }
                    else if (tok[0].equals("%term")) queryMode = QueryTypes.Term;
                    else if (tok[0].equals("%phrase")) queryMode = QueryTypes.Phrase;
                    else if (tok[0].equals("%fuzzy")) queryMode = QueryTypes.Fuzzy;
                    else if (tok[0].equals("%color")) {
                        if (tok[1].equals("on")) showColor = true;
                        else if (tok[1].equals("off")) showColor = false;
                    }
                    else if (tok[0].equals("%details")) {
                        if (tok[1].equals("on")) showDetails = true;
                        else if (tok[1].equals("off")) showDetails = false;
                    }
                    else if (tok[0].equals("%limit")) {
                        try {
                            Integer stoi = Integer.parseUnsignedInt(tok[1]);
                            if (stoi == 0) {
                                stoi = Integer.MAX_VALUE;
                            }
                            resultsLimit = stoi;
                        } catch (Exception ignore) {}
                    }
                    else {
                        Query current;
                        if (queryMode == QueryTypes.Term) {
                            current = new TermQuery(new Term("contents", tok[0]));
                        }
                        else if (queryMode == QueryTypes.Phrase) {
                            current = new PhraseQuery(0, "contents", tok);
                        }
                        else {
                            current = new FuzzyQuery(new Term("contents", tok[0]));
                        }
                        SearchResults sr = currentIndex.searchInIndex(current, resultsLimit);
                        terminal.writer().println("Files count: " + sr.totalHits);
                        for (int i=0; i<sr.filepaths.length; i++) {
                            if (showDetails) {
                                terminal.writer().println(sr.filepaths[i] + ":");
                                terminal.writer().println(sr.highlighted[i]);
                            }
                            else {
                                terminal.writer().println(sr.filepaths[i]);
                            }
                        }
                    }
                } catch (UserInterruptException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("FATAL: Failed to start terminal");
            System.exit(1);
        }
    }
}

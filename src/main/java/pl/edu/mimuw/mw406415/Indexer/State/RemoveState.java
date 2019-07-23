package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;
import pl.edu.mimuw.mw406415.Indexer.Crawler.Crawler;
import pl.edu.mimuw.mw406415.Indexer.Crawler.OperationMode;

import java.io.File;

public class RemoveState implements IState {
    public void run(String arg) {
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            File f = new File(arg);
            try {
                m.deleteByFilepath(f.getCanonicalPath());
            } catch (Exception ignore) {}
        }
        Crawler c = new Crawler();
        c.recursiveCrawl(arg, OperationMode.RemoveFromIndex);
    }
}

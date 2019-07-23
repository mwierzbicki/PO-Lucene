package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;
import pl.edu.mimuw.mw406415.Indexer.Crawler.Crawler;
import pl.edu.mimuw.mw406415.Indexer.Crawler.OperationMode;

public class ReindexState implements IState {
    public void run(String pathToMode) {
        Crawler c = new Crawler();
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            String[] paths = m.getAllPaths();
            for (String path : paths) {
                c.recursiveCrawl(path, OperationMode.RemoveFromIndex);
                c.recursiveCrawl(path, OperationMode.AddToIndex);
            }
        }
    }
}

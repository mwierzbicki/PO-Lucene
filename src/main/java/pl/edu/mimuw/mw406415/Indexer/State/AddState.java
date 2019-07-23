package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;
import pl.edu.mimuw.mw406415.Indexer.Crawler.Crawler;
import pl.edu.mimuw.mw406415.Indexer.Crawler.OperationMode;

public class AddState implements IState {
    public void run(String arg) {
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            m.addFilepath(arg);
        }
        Crawler c = new Crawler();
        c.recursiveCrawl(arg, OperationMode.AddToIndex);
    }
}

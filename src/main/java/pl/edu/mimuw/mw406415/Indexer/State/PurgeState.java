package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.LangIndexManager;
import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;

public class PurgeState implements IState {
    public void run(String arg) {
        try (LangIndexManager pl = new LangIndexManager("pl"); LangIndexManager en = new LangIndexManager("en")){
            pl.clear();
            en.clear();
        }
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            m.clear();
        }
    }
}

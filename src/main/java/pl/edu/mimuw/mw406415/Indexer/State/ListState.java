package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;

public class ListState implements IState {
    public void run(String pathToMode) {
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            String[] paths = m.getAllPaths();
            for (String path : paths) {
                System.out.println(path);
            }
        }
    }
}

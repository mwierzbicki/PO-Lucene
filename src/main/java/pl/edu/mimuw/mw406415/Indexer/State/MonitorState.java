package pl.edu.mimuw.mw406415.Indexer.State;

import pl.edu.mimuw.mw406415.IndexManager.MonitorIndexManager;
import pl.edu.mimuw.mw406415.Indexer.WatchDir.WatchDir;

import java.io.File;

public class MonitorState implements IState {
    public void run(String pathToMode) {
        try (MonitorIndexManager m = new MonitorIndexManager()) {
            WatchDir w = new WatchDir();
            String[] paths = m.getAllPaths();
            for (String path : paths) {
                File f = new File(path);
                w.registerAll(f.toPath());
            }
            w.processEvents();
        } catch (Exception e) {
            System.err.println("FATAL: Watch service failed");
            System.exit(1);
        }
    }
}

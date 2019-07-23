package pl.edu.mimuw.mw406415.Indexer;

import pl.edu.mimuw.mw406415.Indexer.State.*;

public class IndexerMain {
    public static void main(String[] args) {
        IState mode = null;
        String pathToPass = null;
        try {
            if (args.length > 0) {
                if (args[0].equals("--purge")) mode = new PurgeState();
                else if (args[0].equals("--reindex")) mode = new ReindexState();
                else if (args[0].equals("--list")) mode = new ListState();
                else if (args.length == 2) {
                    pathToPass = args[1];
                    if (args[0].equals("--add")) mode = new AddState();
                    else if (args[0].equals("--rm")) mode = new RemoveState();
                    else throw new IllegalArgumentException();
                }
                else throw new IllegalArgumentException();
            } else mode = new MonitorState();
        }
        catch (IllegalArgumentException e) {
            System.err.println("FATAL: Invalid arguments");
            System.exit(1);
        }
        try {
            mode.run(pathToPass);
        }
        catch (IllegalArgumentException e) {
            System.err.println("FATAL: Invalid file path");
            System.exit(1);
        }
        catch (NullPointerException e) {
            System.err.println("FATAL: Unhandled case");
            System.exit(1);
        }
    }
}

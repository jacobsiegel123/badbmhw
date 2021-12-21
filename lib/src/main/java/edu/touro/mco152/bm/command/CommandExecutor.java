package edu.touro.mco152.bm.command;

import java.io.IOException;

/**
 * this is the executor class
 */
public class CommandExecutor {
    /**
     * takes in a command of either read or write
     * @param command takes in the commandInterface
     * @throws IOException
     */
    public void execute(CommandInterface command) throws IOException {
        command.execute();
    }
}

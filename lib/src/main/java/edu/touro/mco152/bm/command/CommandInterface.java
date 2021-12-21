package edu.touro.mco152.bm.command;

import edu.touro.mco152.bm.IDiskAppWorker;

import java.io.IOException;

/**
 * the command interface which read and write benchmark implement
 */
public interface CommandInterface {
    void execute() throws IOException;
}

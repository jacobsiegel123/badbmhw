package edu.touro.mco152.bm.command;

import edu.touro.mco152.bm.IDiskAppWorker;

import java.io.IOException;

public interface CommandInterface {
    void execute(IDiskAppWorker worker) throws IOException;
}

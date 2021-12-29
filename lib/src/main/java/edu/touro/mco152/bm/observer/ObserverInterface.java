package edu.touro.mco152.bm.observer;

import edu.touro.mco152.bm.persist.DiskRun;

/**
 * interface for the observer
 */
public interface ObserverInterface {
    void update(DiskRun run);
}

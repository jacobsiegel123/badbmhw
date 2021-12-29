package edu.touro.mco152.bm;

import edu.touro.mco152.bm.observer.ObserverInterface;
import edu.touro.mco152.bm.persist.DiskRun;

public class TestObserver implements ObserverInterface {
    /**
     * class to test the observer pattern
     */
    boolean test = false;
    @Override
    public void update(DiskRun run) {
        test = true;
    }
    public boolean flag(){
        return test;
    }
}

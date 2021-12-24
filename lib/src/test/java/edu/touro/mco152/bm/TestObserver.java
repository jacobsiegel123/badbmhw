package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

public class TestObserver implements ObserverInterface{
    boolean test = false;
    @Override
    public void update(DiskRun run) {
        test = true;
    }
    public boolean flag(){
        return test;
    }
}

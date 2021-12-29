package edu.touro.mco152.bm.observer;

import edu.touro.mco152.bm.persist.DiskRun;

/**
 * rules for the observer to follow if update is called
 */
public class ObserverRules implements ObserverInterface{
    /**
     * sends a slack message
     * @param run takes in a DiskRun
     */
    @Override
    public void update(DiskRun run) {
        if(run.getRunMax() > run.getRunAvg() * 1.03){
            SlackManager sm = new SlackManager("YS BadBM");
            sm.postMsg2OurChannel(":astonished: Benchmark run was more than the average run :astonished:");

        }

    }
}

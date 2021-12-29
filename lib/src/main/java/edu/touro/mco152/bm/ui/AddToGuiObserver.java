package edu.touro.mco152.bm.ui;

import edu.touro.mco152.bm.observer.ObserverInterface;
import edu.touro.mco152.bm.persist.DiskRun;

public class AddToGuiObserver implements ObserverInterface {
    /**
     * updates the run panel
     * @param run takes in a DiskRun
     */
    @Override
    public void update(DiskRun run) {
        Gui.runPanel.addRun(run);
    }
}

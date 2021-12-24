package edu.touro.mco152.bm.ui;

import edu.touro.mco152.bm.ObserverInterface;
import edu.touro.mco152.bm.persist.DiskRun;

public class AddToGuiObserver implements ObserverInterface {

    @Override
    public void update(DiskRun run) {
        Gui.runPanel.addRun(run);
    }
}

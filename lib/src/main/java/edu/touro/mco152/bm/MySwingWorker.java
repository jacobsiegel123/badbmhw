package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;

import java.beans.PropertyChangeListener;
import java.util.List;

import static edu.touro.mco152.bm.App.dataDir;


public class MySwingWorker extends SwingWorker<Boolean, DiskMark> implements IDiskAppWorker {
    CallabaleInterface callabaleInterface;
    @Override
    public void progressSetter(int i) {
        setProgress(i);
    }

    @Override
    public void publishData(DiskMark m) {
        publish(m);
    }

    @Override
    public void codeToExecute() {
        execute();
    }

    @Override
    public void changeListenerForProperties(PropertyChangeListener pcl) {
        addPropertyChangeListener(pcl);
    }

    @Override
    public boolean wasCanceled() {
        return isCancelled();
    }

    @Override
    public boolean letsCancel(boolean b) {
        return cancel(b);
    }

    @Override
    public void setCallable(CallabaleInterface c) {
        this.callabaleInterface = c;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return callabaleInterface.execute();
    }
    /**
     * Process a list of 'chunks' that have been processed, ie that our thread has previously
     * published to Swing. For my info, watch Professor Cohen's video -
     * Module_6_RefactorBadBM Swing_DiskWorker_Tutorial.mp4
     * @param markList a list of DiskMark objects reflecting some completed benchmarks
     */

    protected void process(List<DiskMark> markList) {
        markList.stream().forEach((dm) -> {
            if (dm.type == DiskMark.MarkType.WRITE) {
                Gui.addWriteMark(dm);
            } else {
                Gui.addReadMark(dm);
            }
        });
    }


    protected void done() {
        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }

}

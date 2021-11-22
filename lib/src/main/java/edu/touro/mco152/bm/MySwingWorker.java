package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static edu.touro.mco152.bm.App.dataDir;

public class MySwingWorker extends SwingWorker<Boolean, DiskMark> implements IDiskSwingApp{
    CallabaleInterface callabaleInterface;
    @Override
    protected Boolean doInBackground() throws Exception {
        return callabaleInterface.execute();
    }


    @Override
    public boolean CancelledCode() {
        return isCancelled();
    }

    @Override
    public void progressSet(int a) {
        setProgress(a);
    }

    @Override
    public void publishData(DiskMark d) {
        publish(d);
    }

    @Override
    public boolean cancelCode(boolean n) {
        return cancel(n);
    }

    @Override
    public void addChangeListenerForProperties(PropertyChangeListener pcl) {
        addPropertyChangeListener(pcl);
    }

    @Override
    public void executeCode() {
        execute();
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

    @Override
    public void setCallable(CallabaleInterface callabaleInterface){
        this.callabaleInterface = callabaleInterface;
    }
}

package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;

public interface IDiskAppWorker {

    void progressSetter(int i);

    void publishData(DiskMark m);

    void codeToExecute();

    void changeListenerForProperties(PropertyChangeListener pcl);

    boolean wasCanceled();

    boolean letsCancel(boolean b);

    void setCallable(CallabaleInterface c);
}

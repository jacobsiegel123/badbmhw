package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;

public interface IDiskAppWorker {

    void progressSet(int i);

    void publishData(DiskMark m);

    void executeCode();

    void addChangeListenerForProperties(PropertyChangeListener pcl);

    boolean hasBeenCanceled();

    boolean pleaseCancel(boolean b);

    void setCallable(CallabaleInterface c);
}

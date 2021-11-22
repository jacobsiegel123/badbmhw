package edu.touro.mco152.bm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface IDiskSwingApp {
    boolean CancelledCode();

    void progressSet(int a);

    void publishData(DiskMark d);

    boolean cancelCode(boolean n);

    //void addPropertyChangeListener(final PropertyChangeEvent event);

    void addChangeListenerForProperties(PropertyChangeListener pcl);

    void executeCode();
    void setCallable(CallabaleInterface c);

}

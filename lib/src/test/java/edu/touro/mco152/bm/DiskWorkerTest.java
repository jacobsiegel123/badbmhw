package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiskWorkerTest implements IDiskAppWorker {
    DiskWorker diskWorker = new DiskWorker(this);
    int percentComplete;
    DiskMark mark;
    boolean temp;

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */
    @BeforeAll
    static void setupDefaultAsPerProperties() {
        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath() + File.separator + App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        } else {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }


    @Override
    public void progressSetter(int i) {
        percentComplete = i;

    }

    @Override
    public void publishData(DiskMark m) {
        mark = m;
    }

    @Override
    public void codeToExecute() {

    }


    @Override
    public void changeListenerForProperties(PropertyChangeListener pcl) {

    }

    @Override
    public boolean wasCanceled() {
        return false;
    }


    @Override
    public boolean letsCancel(boolean b) {
        return false;
    }

    @Override
    public void setCallable(CallabaleInterface c) {

    }

    @Test
    void doInBackground() {
    }

    @Test
    void execute() {
    }

    @Test
    void progressSetter() throws Exception {
//        App.startBenchmark();
//        diskWorker.executeCode();

        diskWorker.execute();


        temp = percentComplete > 0;
        System.out.println(percentComplete + "this is percent complete");
        assertTrue(temp);
        System.out.println("yay the test works");
    }

    @Test
    void publishData() throws Exception {
        diskWorker.execute();
        double temp = mark.getCumAvg();
        assertTrue(temp > 0);

    }
}
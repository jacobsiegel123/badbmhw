package edu.touro.mco152.bm;

import edu.touro.mco152.bm.command.CommandExecutor;
import edu.touro.mco152.bm.command.ReadBenchmark;
import edu.touro.mco152.bm.command.WriteBenchmark;
import edu.touro.mco152.bm.persist.AddToDatabaseObserver;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.AddToGuiObserver;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.blockSequence;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObserverTest implements IDiskAppWorker {

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */

    private final CommandExecutor executor = new CommandExecutor();
    private static boolean readTest =false;
    private static boolean writeTest =false;

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
    /**
     * sets up code to see if observer is running properly for the read tests
     * @throws IOException
     */
    @Test
    public void readBenchmarkObserverTest() throws IOException {
        ReadBenchmark readBenchmark = new ReadBenchmark(this,25,128,2048, DiskRun.BlockSequence.SEQUENTIAL);
        readBenchmark.register(new AddToDatabaseObserver());
        TestObserver observerTest = new TestObserver();

        readBenchmark.register(observerTest);

        executor.execute(readBenchmark);
        readTest = observerTest.flag();

    }

    /**
     * sets up code to see if observer is running properly for the write tests
     * @throws IOException
     */
@Test
   public void writeBenchmarkObserverTest() throws IOException {
        WriteBenchmark writeBenchmark = new WriteBenchmark(this,25,128,2048, DiskRun.BlockSequence.SEQUENTIAL);
        writeBenchmark.register(new AddToDatabaseObserver());
         TestObserver observerTest = new TestObserver();

        writeBenchmark.register(observerTest);

        executor.execute(writeBenchmark);
        writeTest = observerTest.flag();

    }

    /**
     * asserts to see if read and write test worked
     */
    @AfterAll
    @Test
    static void ifFlagged(){
        assertTrue(readTest);
        assertTrue(writeTest);
    }
    @Override
    public void progressSetter(int i) {

    }

    @Override
    public void publishData(DiskMark m) {

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
}
